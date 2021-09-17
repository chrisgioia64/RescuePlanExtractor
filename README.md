## Overview

The goal of this project is to extract state and local recovery funds (available online) into a summarized form (csv files).

Pages 6-8 of the [recovery template document](pdf/template/ReportTemplate.pdf) provide a table of expenses by expenditure category that _should_ be reproduced in some form for each of the state and local reports. This project uses text extraction to pull this table out of each report pdf.

## How to Run

To download all pdfs of states from [here](https://www.nasbo.org/mainsite/resources/covid-19-relief-funds-guidance-and-resources/state-recovery-plans) into the `pdf` directory, run (might need to install some python dependencies):

    python dataloader.py

Check the console or the `logs` to see if there are any `MISSING` pdfs, and add those manually from [here](https://www.nasbo.org/mainsite/resources/covid-19-relief-funds-guidance-and-resources/state-recovery-plans).

_Note:_ This project will work only on Windows.
