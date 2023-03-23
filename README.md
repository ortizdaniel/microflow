# Microflow

Allows you to create diagrams as seen in the second semester of Digital Systems and Microprocessors.
Compatible with Windows, macOS and Linux.

![Microflow screenshot](https://i.imgur.com/fqgZhHb.png)

---
## How to use

**Download:** head over to the [releases](https://github.com/ortizdaniel/microflow/releases) section and download the latest version.

How to use:
- **Windows:** use either the .exe or the .jar version. You can ignore the security warnings given by Windows. I am sadly not a verified developer for Windows.
- **macOS:** use only the .jar version. If you try double-clicking to open it, macOS will not allow you. In order to open it, you have to right-click and then click open. It will say something along the lines of "this app may not be secure" and you have to click "open anyways". I am sadly not a verified developer for macOS.


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
