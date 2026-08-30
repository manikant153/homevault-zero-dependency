# HomeVault Standard Library Report

## Zero-dependency statement

HomeVault has zero third-party runtime dependencies.

The application is built and run using only:

- Java Development Kit (JDK) 17 or later.
- Java Platform Standard Library packages.
- Windows batch scripts for local compilation and execution.

No Maven dependencies, Gradle dependencies, external `.jar` files, frameworks, database drivers, web services, or third-party libraries are included.

---

## Dependency manifest

HomeVault intentionally has no:

```text
pom.xml
build.gradle
settings.gradle
package.json
requirements.txt
external .jar files
```

Compilation is performed directly with:

```powershell
javac -d out src\homevault\*.java
```

Execution is performed directly with:

```powershell
java -cp out homevault.Main
```

---

## Standard-library replacements

| Common third-party choice | Normal purpose | HomeVault zero-dependency replacement |
|---|---|---|
| Jackson / Gson | JSON serialization and parsing | Custom CSV parsing and Java `Property` objects |
| OpenCSV / Apache Commons CSV | CSV reading and writing | `java.io.BufferedReader`, `java.io.BufferedWriter`, `java.nio.file.Files`, and custom parsing with `String.split()` |
| SQLite JDBC / H2 / MongoDB Driver | Persistent structured storage | Custom CSV-backed local persistence using `java.nio.file.Path`, `Files`, and `StandardCopyOption` |
| Apache Commons CLI / Picocli | Command-line option parsing | Custom command tokenizer and parser in `Shell.java` |
| Apache Lucene | Property search/indexing | In-memory filtering with `ArrayList`, `LinkedHashMap`, and custom filtering logic |
| Weka / Smile / scikit-learn | House-price prediction / machine learning | Custom comparable-property selection and transparent price-per-square-foot estimation in `PredictionEngine.java` |
| JUnit / TestNG | Automated tests | Custom assertion-based test runner in `TestRunner.java` |
| Apache Commons Codec | Hashing/encoding helpers | Not needed in current version; Java `java.security.MessageDigest` is available if checksum support is added |

---

## Java standard-library packages used

| Package | Use in HomeVault |
|---|---|
| `java.io` | Read terminal input and CSV files; write data files |
| `java.nio.file` | File paths, checking file existence, creating folders, reading/writing files, safe file replacement |
| `java.util` | Lists, maps, collections, comparators, statistics |
| `java.util.regex` | Tokenize commands and support quoted multi-word locations |
| `java.util.stream` | Property price and area summary calculations |
| `java.lang` | Core Java types, strings, math operations, error handling |

---

## Custom implementations

### Command parser

HomeVault accepts commands such as:

```text
search --location "Greater Noida" --bedrooms 3
predict --location Noida --area 1300 --bedrooms 3 --bathrooms 2 --age 4
```

Instead of using Picocli or Apache Commons CLI, it tokenizes options manually with `java.util.regex.Pattern` and `Matcher`.

### CSV importer

Instead of using OpenCSV, HomeVault:

1. Reads the input CSV line-by-line.
2. Splits fields using commas.
3. Validates column count and number formats.
4. Creates `Property` objects.
5. Reports invalid records instead of crashing.

The supported CSV format is intentionally simple: fields must not contain embedded commas.

### Local persistence engine

Instead of SQLite or an external database driver, HomeVault:

1. Stores properties in a `LinkedHashMap<String, Property>`.
2. Uses property IDs as unique keys.
3. Writes data to `data/properties-temp.csv`.
4. Replaces `data/properties.csv` after a successful write.
5. Loads saved data automatically when the application starts.

### House-price estimator

Instead of an external ML framework, HomeVault implements:

1. Same-location candidate selection.
2. Explainable similarity score based on area, bedrooms, bathrooms, and age.
3. Selection of up to three closest comparable properties.
4. Average comparable price per square foot.
5. Transparent adjustments and confidence rating.

---

## Verification steps

A reviewer can verify zero external dependencies with the following steps.

### 1. Inspect project files

Confirm that the repository has no external dependency manifest or `.jar` file:

```powershell
dir
dir src\homevault
```

### 2. Compile directly with the JDK

```powershell
.\build.bat
```

Expected:

```text
Build successful.
```

### 3. Run the application directly

```powershell
.\run.bat
```

### 4. Run built-in tests

```powershell
java -cp out homevault.TestRunner
```

Expected:

```text
Tests passed: 9
Tests failed: 0
```

### 5. Inspect imports

All source imports begin with standard Java namespaces such as:

```text
java.io
java.nio.file
java.util
java.util.regex
```

No third-party import is used.

---

## Design choice

The purpose of HomeVault is not to argue that libraries are bad. Libraries are valuable in real production systems. This project demonstrates that a useful and understandable data tool can still be engineered with a language’s built-in capabilities when a zero-dependency environment demands it.
