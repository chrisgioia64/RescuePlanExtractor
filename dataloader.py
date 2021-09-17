import os
import sys
import urllib.request
import gdown
import requests
from bs4 import BeautifulSoup
import logging
import re
from datetime import datetime

# Constants
STATE_DIR = "pdf/states/"
LOGS_DIR = "logs/"

def setup_logging():
  ### Setup logging
  now = datetime.now()
  date_time = now.strftime("%Y-%m-%d %H-%M-%S")

  logger = logging.getLogger()
  handler = logging.FileHandler(mode="w", filename=LOGS_DIR +"/" + date_time + ".txt")
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

  def __init__(self, save_dir):
    self.save_dir = save_dir
    self.logger = setup_logging()

  def download_file(self, link):    
    filename = link.text + ".pdf"
    if contains_file(self.save_dir, filename):
      self.logger.info("      %s -- ALREADY EXISTS", filename)
    else:
      try:
        gdown.download(link['href'], self.save_dir + "/" + filename)
      except Exception as err:
        self.logger.debug("     %s -- could not download", filename);
      if contains_file(self.save_dir, filename):
        self.logger.info("      %s -- ADDED", filename)
      else:
        self.logger.info("      %s -- MISSING", filename)
            
    #self.logger.info('%-25s %s', link.text, link['href'])

  def execute(self):
    r = requests.get("https://www.nasbo.org/mainsite/resources/covid-19-relief-funds-guidance-and-resources/state-recovery-plans")    
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

loader = StateLoader(STATE_DIR)
loader.execute()