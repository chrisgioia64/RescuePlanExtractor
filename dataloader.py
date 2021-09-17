import os
import sys
import urllib.request
import gdown
import requests
from bs4 import BeautifulSoup
import logging
import re
from datetime import datetime
import subprocess

# Constants
TEXT_DIR = "text/"
PDF_DIR = "pdf/"
STATES_DIR = "states/"
LOGS_DIR = "logs/"


def setup_logging():
    # Setup logging
    os.makedirs(LOGS_DIR, exist_ok=True)
    now = datetime.now()
    date_time = now.strftime("%Y-%m-%d %H-%M-%S")

    logger = logging.getLogger()
    handler = logging.FileHandler(
        mode="w", filename=LOGS_DIR + "/" + date_time + ".txt")
    handler.setLevel(logging.INFO)
    logger.addHandler(handler)
    logger.setLevel(logging.INFO)

    handler = logging.StreamHandler(sys.stdout)
    handler.setLevel(logging.DEBUG)
    logger.addHandler(handler)
    return logger


def contains_file(directory, filename):
    created_files = os.listdir(directory)
    found = False
    for created_file in created_files:
        if created_file == filename:
            found = True
    return found


class StateLoader:

    def __init__(self, dir):
        self.pdf_dir = PDF_DIR + dir
        self.text_dir = TEXT_DIR + dir
        self.logger = setup_logging()

    def download_file(self, link):
        pdf_file = link.text + ".pdf"
        text_file = link.text + ".txt"
        if contains_file(self.pdf_dir, pdf_file):
            # self.logger.info("      %s -- ALREADY EXISTS", pdf_file)
            pass
        else:
            try:
                if not (".doc" in link['href'] or ".html" in link['href'] or ".asp" in link['href']):
                    gdown.download(
                        link['href'], self.pdf_dir + "/" + pdf_file, quiet=False)
                else:
                    self.logger.debug(
                        "The following link is not a pdf: %s", link['href'])
            except Exception as err:
                self.logger.debug("Could not download  %s", pdf_file)
            if contains_file(self.pdf_dir, pdf_file):
                self.logger.debug("Added               %s", pdf_file)
            else:
                self.logger.info("Missing             %s", pdf_file)
                return
        if contains_file(self.text_dir, text_file):
            pass
        else:
            self.convert_to_pdf(self.pdf_dir + pdf_file,
                                self.text_dir + text_file)

    def convert_to_pdf(self, pdf_file, text_file):
        try:
            subprocess.check_call(
                ['lib\pdftotext.exe', '-q', '-layout', pdf_file, text_file])
            self.logger.debug("Successfully converted  " + pdf_file)
        except subprocess.CalledProcessError as err:
            self.logger.info("Could not convert   " + pdf_file)

    def execute(self):
        os.makedirs(self.pdf_dir, exist_ok=True)
        os.makedirs(self.text_dir, exist_ok=True)
        r = requests.get(
            "https://www.nasbo.org/mainsite/resources/covid-19-relief-funds-guidance-and-resources/state-recovery-plans")
        soup = BeautifulSoup(r.text, "html.parser")
        links = soup.find_all("a")

        processing = False
        for link in links:
            if link.text == "Alabama":
                self.download_file(link)
                processing = True
            elif link.text == "Back to Top":
                processing = False
            elif processing:
                self.download_file(link)


loader = StateLoader(STATES_DIR)
loader.execute()
