from __future__ import annotations

import argparse
import subprocess
import tempfile
from io import BytesIO
from pathlib import Path

from pypdf import PdfReader, PdfWriter
from reportlab.lib.utils import ImageReader
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.pdfgen import canvas


INDEXING_LINES = [
    "一种解决英语动名词搭配错误的模型",
    "Model to Solve English Verb-Noun Collocation Errors",
    "杜一民; 吴桂兴; 吴敏",
    "Yimin Du; Guixing Wu; Min Wu",
    "计算机科学, 2016, 43(7): 230-233",
    "Computer Science, 2016, 43(7): 230-233",
    "DOI: 10.11896/j.issn.1002-137X.2016.07.041",
    "Keywords: verb-noun collocation errors; grammatical error correction; collocation similarity; language model",
]

METADATA = {
    "/Title": "一种解决英语动名词搭配错误的模型",
    "/Author": "杜一民; 吴桂兴; 吴敏",
    "/Subject": "计算机科学, 2016, 43(7): 230-233",
    "/Keywords": "动名词搭配错误; 英语语法纠错; verb-noun collocation errors; grammatical error correction; language model; 10.11896/j.issn.1002-137X.2016.07.041",
    "/Creator": "Searchable PDF preparation; original page images preserved; first-page indexing text layer corrected",
}

FONT_CANDIDATES = [
    Path("C:/Windows/Fonts/simhei.ttf"),
    Path("C:/Windows/Fonts/NotoSansSC-VF.ttf"),
    Path("C:/Windows/Fonts/msyh.ttc"),
]


def find_cjk_font() -> Path:
    for font_path in FONT_CANDIDATES:
        if font_path.exists():
            return font_path
    raise FileNotFoundError("No CJK-capable font found for PDF text layer generation")


def draw_invisible_line(
    page: canvas.Canvas,
    value: str,
    x: float,
    y: float,
    font_name: str,
    font_size: float,
) -> None:
    text = page.beginText()
    text.setTextOrigin(x, y)
    text.setFont(font_name, font_size)
    text.setTextRenderMode(3)
    text.textLine(value)
    page.drawText(text)


def render_first_page(source: Path, output_dir: Path) -> Path:
    prefix = output_dir / "first-page"
    subprocess.run(
        [
            "pdftoppm",
            "-f",
            "1",
            "-l",
            "1",
            "-jpeg",
            "-jpegopt",
            "quality=92",
            "-r",
            "200",
            str(source),
            str(prefix),
        ],
        check=True,
    )
    rendered = output_dir / "first-page-1.jpg"
    if not rendered.exists():
        raise FileNotFoundError(f"Expected rendered first page at {rendered}")
    return rendered


def make_clean_first_page(source: Path, width: float, height: float) -> PdfReader:
    with tempfile.TemporaryDirectory() as temp_root:
        temp_dir = Path(temp_root)
        first_page_image = render_first_page(source, temp_dir)

        packet = BytesIO()
        font_path = find_cjk_font()
        cjk_font = "IndexingCJK"
        pdfmetrics.registerFont(TTFont(cjk_font, str(font_path)))

        page = canvas.Canvas(packet, pagesize=(width, height))
        page.drawImage(
            ImageReader(str(first_page_image)),
            0,
            0,
            width=width,
            height=height,
            preserveAspectRatio=False,
            mask="auto",
        )

        y = height - 34
        for line in INDEXING_LINES:
            font_name = cjk_font if any(ord(char) > 127 for char in line) else "Helvetica"
            draw_invisible_line(page, line, 44, y, font_name, 9)
            y -= 11

        page.save()
        packet.seek(0)
        return PdfReader(packet)


def add_indexing_text_layer(source: Path, output: Path) -> None:
    reader = PdfReader(str(source))
    writer = PdfWriter()

    for page_number, page in enumerate(reader.pages):
        if page_number == 0:
            width = float(page.mediabox.width)
            height = float(page.mediabox.height)
            writer.add_page(make_clean_first_page(source, width, height).pages[0])
        else:
            writer.add_page(page)

    writer.add_metadata(METADATA)
    output.parent.mkdir(parents=True, exist_ok=True)
    with output.open("wb") as handle:
        writer.write(handle)


def main() -> None:
    parser = argparse.ArgumentParser(
        description="Add a clean first-page OCR-style indexing text layer to the paper PDF."
    )
    parser.add_argument("source", type=Path)
    parser.add_argument("output", type=Path)
    args = parser.parse_args()

    add_indexing_text_layer(args.source, args.output)


if __name__ == "__main__":
    main()
