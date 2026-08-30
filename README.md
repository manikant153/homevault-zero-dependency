# HomeVault

> An offline, zero-dependency Java CLI for local property storage, analysis, and explainable house-price estimation.

HomeVault helps users import property records from a CSV file, search and filter listings, calculate local price statistics, and estimate a property price from comparable houses.

It is designed as a local-first educational decision-support tool. HomeVault runs entirely on a computer using the Java standard library only: no frameworks, external packages, database drivers, cloud services, or runtime internet connection.

---

## Why HomeVault?

Property records are often stored as simple CSV files. A user may want to:

- Explore properties by location, area, bedrooms, or price range.
- Compare houses in the same locality.
- Calculate local average prices and price per square foot.
- Get a transparent estimate for a new property.
- Keep data private and available offline.
- Avoid setting up a database server or downloading third-party software libraries.

HomeVault provides these features through a simple command-line interface.

---

## Features

- Import property records from CSV files.
- Validate CSV records and report rejected rows.
- Prevent duplicate records using unique property IDs.
- List all stored properties in a readable table.
- Search using multiple filters:
  - Location
  - Bedrooms
  - Minimum and maximum price
  - Minimum and maximum area
- Support quoted multi-word locations such as `"Greater Noida"`.
- Calculate overall and location-wise property statistics.
- Calculate average price per square foot.
- Estimate house prices using comparable local properties.
- Show comparable houses, similarity scores, adjustments, and confidence level.
- Save property data locally and reload it automatically after restart.
- Run core tests without JUnit or any external test framework.

---

## Requirements

- Java Development Kit (JDK) 17 or later.
- Windows PowerShell or Command Prompt.

Check Java installation:

```powershell
java -version
javac -version
```

---

## Zero-dependency guarantee

HomeVault uses only the Java Development Kit standard library.

It does not use:

- Maven or Gradle dependencies.
- External `.jar` files.
- Spring Boot.
- Gson or Jackson.
- OpenCSV or Apache Commons CSV.
- SQLite JDBC, MySQL, MongoDB, or any database driver.
- JUnit or another testing framework.
- Cloud APIs, web APIs, or runtime internet access.

See [STDLIB.md](STDLIB.md) for details of standard-library replacements.

---

## Project structure

```text
HomeVault/
├── src/
│   └── homevault/
│       ├── Main.java
│       ├── Shell.java
│       ├── Property.java
│       ├── PropertyRepository.java
│       ├── CsvImporter.java
│       ├── ImportResult.java
│       ├── SearchOptions.java
│       ├── PropertySearchService.java
│       ├── PropertyStatistics.java
│       ├── StatisticsService.java
│       ├── PredictionRequest.java
│       ├── ComparableProperty.java
│       ├── PredictionResult.java
│       ├── PredictionEngine.java
│       ├── PersistenceManager.java
│       └── TestRunner.java
├── examples/
│   └── noida-houses.csv
├── data/
│   └── properties.csv              Generated after importing data
├── docs/
│   ├── architecture.md
│   └── demo-script.md
├── build.bat
├── run.bat
├── README.md
└── STDLIB.md
```

---

## Build

From the project root, run:

```powershell
.\build.bat
```

Expected output:

```text
Build successful.
Run application: java -cp out homevault.Main
Run tests: java -cp out homevault.TestRunner
```

---

## Run

Run HomeVault with:

```powershell
.\run.bat
```

On first launch, import the sample dataset:

```text
homevault> import examples/noida-houses.csv
```

HomeVault saves the records to `data/properties.csv`.

On later launches, it automatically loads saved data:

```text
Loaded 12 saved properties.
```

---

## Commands

### Help

```text
help
```

Shows the full command list and examples.

### Import CSV data

```text
import examples/noida-houses.csv
```

Expected CSV format:

```csv
id,location,areaSqFt,bedrooms,bathrooms,ageYears,price
N001,Noida,900,2,2,6,5100000
N002,Noida,1050,2,2,4,5900000
```

### List properties

```text
list
```

### Search properties

```text
search --location Noida
search --location Noida --bedrooms 3
search --location Noida --bedrooms 3 --min-price 6000000 --max-price 9000000
search --min-area 1000 --max-area 1400
search --location "Greater Noida" --min-area 1000
```

### View statistics

```text
stats
stats --location Noida
stats --location "Greater Noida"
```

Statistics include:

- Number of properties analysed.
- Average price.
- Minimum and maximum price.
- Average area.
- Average price per square foot.

### Predict house price

```text
predict --location Noida --area 1300 --bedrooms 3 --bathrooms 2 --age 4
```

Example:

```text
Estimated price: INR 7,561,123
Prediction confidence: High
```

The output also shows the comparable properties, average comparable price per square foot, base estimate, feature adjustments, and confidence level.

### Save and exit

```text
save
exit
```

HomeVault automatically saves data after an import and when closing.

---

## Prediction approach

HomeVault uses a transparent comparable-property approach.

1. It finds properties in the requested location.
2. It gives each candidate a similarity score based on:
   - Area difference.
   - Bedroom difference.
   - Bathroom difference.
   - Property age difference.
3. It selects up to three most similar properties.
4. It calculates their average price per square foot.
5. It calculates a base price:

\[
\text{Base Estimate} =
\text{Requested Area} \times
\text{Average Comparable Price Per Sq Ft}
\]

6. It applies small, visible adjustments for differences in bedrooms, bathrooms, and age.

\[
\text{Final Estimate} =
\text{Base Estimate}
+ \text{Bedroom Adjustment}
+ \text{Bathroom Adjustment}
+ \text{Age Adjustment}
\]

This model is intentionally simple and explainable. It is an educational estimate based solely on local imported data; it is not professional real-estate or financial advice.

---

## Data persistence

HomeVault stores imported records locally in:

```text
data/properties.csv
```

To reduce the chance of partial writes, HomeVault first writes to a temporary file and then replaces the saved data file.

Duplicate property IDs are skipped during import.

---

## Run tests

Build first:

```powershell
.\build.bat
```

Then run:

```powershell
java -cp out homevault.TestRunner
```

Expected result:

```text
Tests passed: 9
Tests failed: 0
```

---

## Demo workflow

A recommended demo sequence:

```text
1. Run .\run.bat
2. Import examples/noida-houses.csv
3. Search Noida properties
4. Show statistics for Noida
5. Predict a price for a new Noida property
6. Show comparable properties and confidence
7. Exit HomeVault
8. Run .\run.bat again
9. Show saved data automatically loaded
10. Run zero-dependency tests
```

See [docs/demo-script.md](docs/demo-script.md) for the final video script.

---

## Limitations

- HomeVault is a CLI application; it does not include a graphical interface.
- CSV values must not contain commas inside a field.
- The included dataset is illustrative and small.
- Predictions depend entirely on the imported local dataset.
- It does not fetch live market listings or use external property APIs.
- It is not intended for professional valuation, investment, or legal decisions.

---

## Hackathon compliance

HomeVault was built for the **Zero Dependency | 72-Hour Hackathon**.

- Language: Java
- Build: `.\build.bat`
- Runtime dependencies: None beyond the installed JDK
- External packages/frameworks: None
- Storage: Custom local CSV persistence
- Test framework: Custom in-project `TestRunner`
