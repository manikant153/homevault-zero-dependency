# HomeVault Architecture

## Overview

HomeVault is an offline Java command-line application that imports, stores, searches, analyses, and estimates prices for property records.

```text
                 ┌──────────────────────┐
                 │ User / Terminal CLI  │
                 │ homevault> command   │
                 └──────────┬───────────┘
                            │
                            ▼
                 ┌──────────────────────┐
                 │ Shell.java           │
                 │ Command routing and  │
                 │ input tokenization   │
                 └──────┬─────┬─────┬───┘
                        │     │     │
        ┌───────────────┘     │     └────────────────┐
        ▼                     ▼                      ▼
┌───────────────┐   ┌─────────────────┐   ┌────────────────────┐
│ CsvImporter   │   │ Search/Stats    │   │ PredictionEngine   │
│ CSV validation│   │ Services        │   │ Comparable houses  │
└──────┬────────┘   └────────┬────────┘   └─────────┬──────────┘
       │                     │                        │
       └─────────────────────┴────────────┬───────────┘
                                           ▼
                             ┌─────────────────────────┐
                             │ PropertyRepository      │
                             │ LinkedHashMap by ID      │
                             └───────────┬─────────────┘
                                         │
                                         ▼
                             ┌─────────────────────────┐
                             │ PersistenceManager      │
                             │ data/properties.csv     │
                             └─────────────────────────┘
```

## Main components

| Component | Responsibility |
|---|---|
| `Main` | Starts HomeVault and loads saved property data |
| `Shell` | Reads commands, displays output, parses options |
| `Property` | Represents one property record |
| `PropertyRepository` | Maintains unique properties in memory by ID |
| `CsvImporter` | Imports and validates property CSV data |
| `PropertySearchService` | Filters properties by user-selected criteria |
| `StatisticsService` | Calculates local price and area statistics |
| `PredictionEngine` | Selects comparable houses and creates an explainable estimate |
| `PersistenceManager` | Saves data locally and reloads it at startup |
| `TestRunner` | Runs core tests without a third-party framework |

## Data flow

### Import flow

```text
CSV file
   ↓
CsvImporter validates each row
   ↓
Property objects are created
   ↓
PropertyRepository stores unique IDs
   ↓
PersistenceManager writes data/properties.csv
```

### Prediction flow

```text
predict command
   ↓
Shell parses location, area, bedrooms, bathrooms, and age
   ↓
PredictionEngine finds same-location candidate properties
   ↓
Candidates receive similarity scores
   ↓
Top 3 comparable properties are selected
   ↓
Average comparable price per sq ft is calculated
   ↓
Feature adjustments are applied
   ↓
Explainable estimate and confidence level are printed
```

## Storage design

HomeVault uses local CSV storage rather than an external database:

```text
data/properties.csv
```

The application writes to a temporary file first and replaces the saved file after the write completes. Property IDs are unique, so duplicate CSV imports do not create duplicate records.
