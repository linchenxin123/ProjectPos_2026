# ProjectPos_2026

A Point-of-Sale (POS) system for a small supermarket, built using Object-Oriented Analysis and Design (OOAD) techniques.

## Features

- **Process Sale**: Record items, calculate totals, accept cash payment, update inventory, generate receipts
- **Handle Returns**: Record returned items, process refunds, update inventory, generate return receipts

## Architecture

The system follows a layered architecture with GRASP and GoF design patterns:

### Design Patterns Applied

| Pattern | Type | Application |
|---------|------|-------------|
| Controller (GRASP) | Responsibility | `Register` receives all system events from UI |
| Creator (GRASP) | Responsibility | `Sale` creates `SalesLineItem` objects |
| Information Expert (GRASP) | Responsibility | `SalesLineItem` calculates subtotal; `Sale` calculates total |
| Factory (GoF) | Creational | `ProductCatalog` creates/retrieves `ProductDescription` |
| Adapter (GoF) | Structural | `PrinterAdapter` adapts `ExternalPrinter` to `ReceiptPrinter` |
| Singleton (GoF) | Creational | `Inventory` ensures single instance |
| Value Object | Pattern | `Money` and `ItemID` are immutable value objects |

## Project Structure

```
ProjectPos_2026/
├── src/main/java/pos/
│   ├── model/          # Domain model classes
│   │   ├── Money.java
│   │   ├── ItemID.java
│   │   ├── ProductDescription.java
│   │   ├── SalesLineItem.java
│   │   ├── Sale.java
│   │   ├── ReturnItem.java
│   │   └── Register.java
│   ├── catalog/        # Product catalog (Factory)
│   │   └── ProductCatalog.java
│   ├── payment/        # Payment handling
│   │   └── Payment.java
│   ├── inventory/      # Inventory management (Singleton)
│   │   └── Inventory.java
│   ├── receipt/        # Receipt generation
│   │   └── Receipt.java
│   ├── adapter/        # Adapter pattern
│   │   ├── ReceiptPrinter.java
│   │   ├── ExternalPrinter.java
│   │   └── PrinterAdapter.java
│   └── ui/             # User interface
│       └── POSMain.java
├── src/test/java/pos/
│   └── POSTest.java
├── README.md
└── .gitignore
```

## How to Compile and Run

```bash
# Compile
javac -d out src/main/java/pos/model/*.java src/main/java/pos/catalog/*.java src/main/java/pos/payment/*.java src/main/java/pos/inventory/*.java src/main/java/pos/receipt/*.java src/main/java/pos/adapter/*.java src/main/java/pos/ui/*.java

# Run the POS system
java -cp out pos.ui.POSMain

# Run tests
javac -d out src/test/java/pos/POSTest.java -cp out
java -cp out pos.POSTest
```

## Technologies

- Language: Java (JDK 8+)
- No external dependencies
- Console-based UI
