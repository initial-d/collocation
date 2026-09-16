# Academic indexing notes

This repository publishes a stable landing page and a searchable PDF for:

**一种解决英语动名词搭配错误的模型**

The goal is to make the 2016 paper easier for academic crawlers and readers to
identify without changing the original paper content.

## Public URLs

- Landing page: https://initial-d.github.io/collocation/
- Searchable PDF: https://initial-d.github.io/collocation/papers/model-to-solve-english-verb-noun-collocation-errors.pdf
- DOI: https://doi.org/10.11896/j.issn.1002-137X.2016.07.041

## Current crawler-facing signals

- The PDF URL ends in `.pdf` and is linked from the landing page.
- The current PDF SHA256 is
  `E4D94E827008D346EE71127323C6C72392A81AE187AE30E2AD9C983396F5D41D`.
- The PDF is under 5 MB and its first page begins with a clean, extractable
  bibliographic text layer for title, authors, journal, pages, DOI, and
  keywords.
- The landing page exposes Highwire-style `citation_*` tags for title,
  authors, publication date, journal, ISSN, volume, issue, pages, DOI, language,
  keywords, PDF URL, and abstract URL.
- The page also exposes Dublin Core title, creator, date, identifier, and
  language tags.
- The structured data uses `schema.org/ScholarlyArticle` with authors, DOI,
  journal, issue, pages, language, canonical URL, DBLP record, citation text,
  and PDF encoding.
- The landing page links directly to hosted BibTeX and CFF files.
- `.zenodo.json` and `docs/scholarly_deposit_metadata.md` keep repository,
  Zenodo, OSF, ResearchGate, and institutional repository metadata aligned.
- `robots.txt` allows crawling and points to `sitemap.xml`.
- `sitemap.xml` lists the landing page, PDF, BibTeX file, and CFF file.
- `docs/google0ef880f8ffde0761.html` verifies the URL-prefix property in
  Google Search Console.

## Maintenance checklist

- Keep the publication date as `2016/07`; do not replace it with the repository
  upload date.
- Keep one stable landing page per paper.
- Keep the searchable PDF at the current URL unless a redirect is added.
- If the PDF is regenerated, verify that its first page still exposes the
  Chinese title and authors as selectable text.
- If the publisher URL changes, update both the visible DOI link and structured
  metadata.
- If a stable article record appears in another scholarly index, add it as a
  visible bibliography link only after confirming it resolves reliably.
- Do not add private notes, internal paths, access tokens, or unpublished data
  to the landing page or PDF.

## Last checked

- 2026-09-02: landing page metadata, sitemap, robots setup, PDF hash, and
  first-page extractable title/authors reviewed.
- 2026-09-15: added hosted BibTeX/CFF files, a DBLP record link, refreshed the
  sitemap, and strengthened bilingual search metadata.
- 2026-09-15: corrected the first-page PDF indexing text layer while preserving
  the original page image, then verified PDF metadata, size, text extraction,
  and first-page rendering.
- 2026-09-16: added Zenodo-ready repository metadata and a reusable scholarly
  deposit metadata checklist for third-party academic repositories.
- 2026-09-16: added the Google Search Console HTML verification file for the
  `https://initial-d.github.io/collocation/` URL-prefix property.
