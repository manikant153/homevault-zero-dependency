# HomeVault Demo Script

## Goal

Demonstrate that HomeVault is:

- Useful.
- Fully functional.
- Offline and local-first.
- Built using Java standard library only.
- Able to store, search, analyse, predict, save, and reload property data.

Target demo duration: 4 to 5 minutes.

---

## Before recording

1. Open PowerShell in the HomeVault project folder.
2. Make sure the project builds:

```powershell
.\build.bat
java -cp out homevault.TestRunner
```

3. To demonstrate a fresh import, optionally remove local generated data:

```powershell
Remove-Item data\properties.csv
```

4. Keep `examples/noida-houses.csv` visible in VS Code.
5. Increase PowerShell font size so all output is readable.
6. Close unrelated browser tabs, notifications, and private windows.

---

## Demo narration and commands

### 0:00–0:25 — Introduce the project

Say:

> HomeVault is an offline, zero-dependency Java command-line application for local property storage, analysis, and explainable house-price estimation. It uses no frameworks, database drivers, CSV libraries, JSON libraries, or machine-learning libraries.

Show the GitHub repository and briefly show:

```text
src/homevault/
README.md
STDLIB.md
build.bat
run.bat
```

---

### 0:25–0:45 — Prove the build

Run:

```powershell
.\build.bat
```

Say:

> HomeVault compiles directly with javac in one command. There is no Maven, Gradle, or external JAR file.

---

### 0:45–1:20 — Start and import property data

Run:

```powershell
.\run.bat
```

Then:

```text
help
import examples/noida-houses.csv
```

Say:

> I am importing a local CSV dataset containing property ID, location, area, bedrooms, bathrooms, age, and sale price. HomeVault validates every row, reports rejected rows, avoids duplicate IDs, and automatically saves valid data locally.

---

### 1:20–1:50 — Show property search

Run:

```text
search --location Noida --bedrooms 3
```

Then:

```text
search --location "Greater Noida" --min-area 1000
```

Say:

> Users can combine filters such as location, bedroom count, price range, and area. Quoted multi-word locations are supported.

---

### 1:50–2:20 — Show local statistics

Run:

```text
stats --location Noida
```

Say:

> HomeVault calculates local average price, minimum and maximum price, average area, and average price per square foot using only the imported local data.

---

### 2:20–3:30 — Show explainable prediction

Run:

```text
predict --location Noida --area 1300 --bedrooms 3 --bathrooms 2 --age 4
```

Say:

> The estimate is not a black box. HomeVault selects the three most comparable properties in the requested location. It uses area, bedroom count, bathroom count, and property age to calculate a similarity score. It averages the comparable properties’ price per square foot, calculates a base estimate, and shows each adjustment clearly.

Point to:
- Comparable property list.
- Similarity scores.
- Average comparable price per square foot.
- Base estimate.
- Bedroom, bathroom, and age adjustments.
- Final price estimate.
- Confidence level.

Then say:

> The result is an educational estimate based only on the local sample data, not professional property or financial advice.

---

### 3:30–4:00 — Prove persistence

Run:

```text
exit
```

Then start again:

```powershell
.\run.bat
```

Say:

> HomeVault automatically loads the saved property data after restart, proving that it has local persistence without SQLite, MongoDB, or any database driver.

Run:

```text
list
```

---

### 4:00–4:35 — Prove tests

Exit HomeVault if needed:

```text
exit
```

Run:

```powershell
java -cp out homevault.TestRunner
```

Say:

> The repository contains a custom zero-dependency test runner. It tests price-per-square-foot calculation, search, prediction, unknown-location handling, and duplicate-property prevention.

---

### 4:35–5:00 — Prove zero dependencies and close

Open `STDLIB.md`.

Say:

> This document shows common packages that would normally be used, such as OpenCSV, SQLite JDBC, Picocli, Lucene, or Weka. HomeVault replaces them with Java standard-library functionality and custom implementations. The project is built for the Zero Dependency Hackathon and contains no external runtime dependencies.

Close with:

> HomeVault demonstrates that a useful local-first data application can be built with only Java and the standard library.
