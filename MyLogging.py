import os
from datetime import datetime
import logging
import Constants
import sys


def setup_logging():
    # Setup logging
    os.makedirs(Constants.LOGS_DIR, exist_ok=True)
    now = datetime.now()
    date_time = now.strftime("%Y-%m-%d %H-%M-%S")

    logger = logging.getLogger()
    handler = logging.FileHandler(
        mode="w", filename=Constants.LOGS_DIR + "/" + date_time + ".txt")
    handler.setLevel(logging.INFO)
    logger.addHandler(handler)
    logger.setLevel(logging.INFO)

    handler = logging.StreamHandler(sys.stdout)
    handler.setLevel(logging.DEBUG)
    logger.addHandler(handler)
    return logger
