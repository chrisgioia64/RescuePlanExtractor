from abc import abstractmethod
import Constants
import Utils
import gdown
import os
import requests
import bs4
from bs4 import BeautifulSoup


class Loader:
    def __init__(self, dir, logger):
        self.pdf_dir = Constants.PDF_DIR + dir
        self.text_dir = Constants.TEXT_DIR + dir
        self.logger = logger
        os.makedirs(self.pdf_dir, exist_ok=True)
        os.makedirs(self.text_dir, exist_ok=True)

    @abstractmethod
    def execute():
        pass

    def download_file(self, link):
        entityName = link.text.strip()
        pdf_file = entityName + ".pdf"
        text_file = entityName + ".txt"
        if Utils.contains_file(self.pdf_dir, pdf_file):
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
            if Utils.contains_file(self.pdf_dir, pdf_file):
                self.logger.debug("Added               %s", pdf_file)
            else:
                self.logger.info("%-30s (Could not download pdf)", entityName)
                return
        if Utils.contains_file(self.text_dir, text_file):
            pass
        else:
            Utils.convert_to_pdf(self.pdf_dir + pdf_file,
                                 self.text_dir + text_file, self.logger)


class LocalLoader(Loader):

    def execute(self):
        pdf_files = os.listdir(self.pdf_dir)
        for pdf_file in pdf_files:
            items = pdf_file.split(".")
            if len(items) == 2:
                base_file = items[0]
                text_file = base_file + ".txt"
                print("Converting pdf to text: " + base_file)
                Utils.convert_to_pdf(self.pdf_dir + pdf_file,
                                     self.text_dir + text_file, self.logger)
            else:
                self.logger.info("Error: " + pdf_file)


class StateLoader(Loader):

    def execute(self):
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


class TemplateLoader(Loader):

    def execute(self):
        Utils.convert_to_pdf(self.pdf_dir + Constants.REPORT_TEMPLATE + ".pdf",
                             self.text_dir + Constants.REPORT_TEMPLATE + ".txt", self.logger)
