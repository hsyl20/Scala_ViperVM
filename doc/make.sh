#!/bin/bash
tex=ViperVM
inputs=$TEXINPUTS:src/:build/:src/format
bib=src

mkdir -p build
BIBINPUTS=$bib bibtex build/$tex.aux
TEXINPUTS=$inputs pdflatex -output-directory build src/$tex.tex
