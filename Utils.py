import os
import subprocess


def contains_file(directory, filename):
    created_files = os.listdir(directory)
    found = False
    for created_file in created_files:
        if created_file == filename:
            found = True
    return found


def convert_to_pdf(pdf_file, text_file, logger):
    try:
        subprocess.check_call(
            ['lib\pdftotext.exe', '-q', '-layout', pdf_file, text_file])
        logger.debug("Successfully converted  " + pdf_file)
    except subprocess.CalledProcessError as err:
        logger.info("%-30s (Could not convert pdf to text)", pdf_file)
