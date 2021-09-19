import os
import sys
import urllib.request
import gdown
import requests
import logging
import re
from datetime import datetime
import subprocess
import MyLogging
import Constants
import Loader
import Utils
import argparse


parser = argparse.ArgumentParser(
    description='Runs the download, pdf-to-text extraction, and csv writing')

parser.add_argument(
    '-d', help='download the pdfs and runs pdf-text extraction', action="store_true")
parser.add_argument(
    '-o', help='specifies the output csv file')

args = parser.parse_args()

logger = MyLogging.setup_logging()

if not args.o and not args.d:
    logger.info("Use -h to see usage options")

if args.d:
    logger.info("---  Downloading pdfs   ---")
    stateLoader = Loader.StateLoader(Constants.STATES_DIR, logger)
    stateLoader.execute()
    templateLoader = Loader.TemplateLoader(Constants.TEMPLATES_DIR, logger)
    templateLoader.execute()

if args.o:
    logger.info("---  Writing out to a csv file: %s", args.o)
    Utils.create_csv(args.o, logger)
