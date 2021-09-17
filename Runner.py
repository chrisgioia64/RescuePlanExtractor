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

logger = MyLogging.setup_logging()
stateLoader = Loader.StateLoader(Constants.STATES_DIR, logger)
stateLoader.execute()
templateLoader = Loader.TemplateLoader(Constants.TEMPLATES_DIR, logger)
templateLoader.execute()
