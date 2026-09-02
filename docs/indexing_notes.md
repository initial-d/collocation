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
  `A5CD00984D86751FCBE5B4E73B5028AFB31DA7FE26DDF05C23601651B2ED8EC3`.
- The landing page exposes Highwire-style `citation_*` tags for title,
  authors, publication date, journal, ISSN, volume, issue, pages, DOI, language,
  keywords, PDF URL, and abstract URL.
- The page also exposes Dublin Core title, creator, date, identifier, and
  language tags.
- The structured data uses `schema.org/ScholarlyArticle` with authors, DOI,
  journal, issue, pages, language, canonical URL, and PDF encoding.
- `robots.txt` allows crawling and points to `sitemap.xml`.
- `sitemap.xml` lists the landing page and PDF.

## Maintenance checklist

- Keep the publication date as `2016/07`; do not replace it with the repository
  upload date.
- Keep one stable landing page per paper.
- Keep the searchable PDF at the current URL unless a redirect is added.
- If the PDF is regenerated, verify that its first page still exposes the
  Chinese title and authors as selectable text.
- If the publisher URL changes, update both the visible DOI link and structured
  metadata.
- Do not add private notes, internal paths, access tokens, or unpublished data
  to the landing page or PDF.

## Last checked

- 2026-09-02: landing page metadata, sitemap, robots setup, PDF hash, and
  first-page extractable title/authors reviewed.
