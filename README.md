# Microflow

Allows you to create diagrams as seen in the second semester of Digital Systems and Microprocessors.
Compatible with Windows, macOS and Linux.

![Microflow screenshot](https://i.imgur.com/fqgZhHb.png)

---

## How to build

Clone this repository and then execute the following command in your terminal:
```
mvn clean install
```

This will create in the `target` folder three files:
1. Microflow-x.x.x.jar (cross-platform)
2. Microflow-x.x.x.exe (Windows)
3. Microflow-x-x.x.dmg (macOS)

Note: the DMG file will only be built when ran on macOS. On other operating systems, a .app will be built instead.

---

Based on the original BubbleWizard by Francesc.
