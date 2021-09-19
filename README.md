## Overview

The goal of this project is to extract state and local recovery funds (available online) into a summarized form (csv files).

Pages 6-8 of the [recovery template document](pdf/template/ReportTemplate.pdf) provide a table of expenses by expenditure category that _should_ be reproduced in some form for each of the state and local reports. This project uses text extraction to pull this table out of each report pdf.

## How to Run

Step 1: Download the pdf reports (and convert to text)

    python Runner.py -d

Check the console or the `logs` directory (most recent log file) to check for any errors (couldn't download pdf, couldn't convert). You may be able to add some of those files manually (see [State Reports](https://www.nasbo.org/mainsite/resources/covid-19-relief-funds-guidance-and-resources/state-recovery-plans)) into the folder `pdf/states`, and re-run the script.

Step 2: Extract tables from reports

    python Runner.py -o output.csv

Using the downloaded text data from step 1, this will extract the table of expenses and place them into a csv file specified by the `o` argument. Look in `csv\output.csv` for the generated csv file.

_Note:_ This project will work only on Windows.
