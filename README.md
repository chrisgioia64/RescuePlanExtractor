## Overview

The goal of this project is to extract state and local recovery funds (available online) into a summarized form (csv files).

Pages 6-8 of the [recovery template document](pdf/template/ReportTemplate.pdf) provide a table of expenses by expenditure category that _should_ be reproduced in some form for each of the state and local reports. This project uses text extraction to pull this table out of each report pdf.

## How to Run

**Step 1:** First, clone this repository onto your local machine.

**Step 2:** This program uses Java and Python. Satisfy the following prerequisites:

(A) Install Java 8+ on your computer. Verify installation by running `java -version` on the command prompt.

(B) Install all required python dependencies. From within the project directory, execute the following command.

    pip install -r requirements.txt

**Step 3:** Download the pdf reports.

There are two sources of reports.

1. [State Reports](https://www.nasbo.org/mainsite/resources/covid-19-relief-funds-guidance-and-resources/state-recovery-plans)
2. [Local Reports](https://drive.google.com/drive/folders/1ixRAsqCEpsau9l2-NzSkUSzVWmZOUr7s?usp=sharing)

You need to download all files from the _Local Reports_ link and place it in `pdf/local`. The _State Reports_ will be automatically downloaded from the link above.

From within the project directory, run:

    python Runner.py -d

Check the console or the `logs` directory (most recent log file) to check for any errors (couldn't download pdf, couldn't convert). You may be able to add some of those files manually, and re-run the script.

**Step 4:** Extract tables from reports

From within the project directory, run:

    python Runner.py -o output.csv

Using the downloaded reports from step 3, this will extract the table of expenses and place them into a csv file specified by the `o` argument. Look in `csv\output.csv` for the generated csv file.

_Note:_ This project will work only on Windows.
