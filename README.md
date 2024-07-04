# Andes OpenEmbedded/Yocto Layer

This layer provides machine configurations and recipes for building the bootable disk image with AndeSight Linux packages.

## Supported AndesCore™ Processors

- [x] [ae350-a25mp](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-a25mp/)
- [x] [ae350-a27l2](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-a27l2/)
- [x] [ae350-a45mp](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-a45mp/)
- [x] [ae350-ax25mp](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax25mp/)
- [x] [ae350-ax27l2](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax27l2/)
- [x] [ae350-ax45mp](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax45mp/)
- [x] [ae350-ax45mpv](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax45mpv/)
- [x] [ae350-ax65](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax65/)
- [x] [qilai](https://www.andestech.com/tw/2024/05/30/andes-technology-announced-the-qilai-soc-and-the-voyager-development-board/) (experimental)

## Building SD Card Image with kas-container

[kas-container](https://kas.readthedocs.io/en/4.1/userguide.html) provides a Yocto development environment based on the Debian docker image. Before you proceed with the build process, make sure Docker is installed on your host machine.

```
$ mkdir riscv-andes && cd riscv-andes
$ git clone https://github.com/andestech/meta-andes.git -b dev-qilai-scarthgap
$ wget https://raw.githubusercontent.com/siemens/kas/4.1/kas-container
$ chmod a+x ./kas-container
```

AndeSight v5.3.0 includes OpenSBI, U-Boot and Linux source based on the following versions.

* [OpenSBI v1.2](https://github.com/andestech/opensbi/tree/ast-v5_3_0-branch)
* [U-Boot v2023.01](https://github.com/andestech/uboot/tree/ast-v5_3_0-branch)
* [Linux 6.1.47](https://github.com/andestech/linux/tree/ast-v5_3_0-branch)

Qilai OpenSBI, U-Boot, and Linux are based on following versions:

* OpenSBI v1.2
* U-Boot v2022.04
* Linux 6.1.47

To build a Poky reference distribution, take `qilai` as an example:

```
$ ./kas-container build meta-andes/kas/qilai.yml
```

### Build Results

Find the built image, bootloader binaries and boot files generated in **build/tmp/deploy/images/<MACHINE>**, such as

* core-image-base-qilai.rootfs.wic.gz
* Image
* boot.scr.uimg
* uEnv.txt
* qilai.dtb
* u-boot-spl.bin
* u-boot.itb

> qilai.dtb, u-boot-spl.bin and u-boot.itb need to be programmed onto flash using `qilai_flash_kit`.

## Flashing Image to SD Card

Use the Linux `dd` command to flash the image to an SD card.

```
$ gunzip -c <IMAGE>.wic.gz | sudo dd of=/dev/sdX bs=4M iflag=fullblock oflag=direct conv=fsync status=progress && sync
```

You can also use the [belenaEther](https://www.balena.io/etcher/) GUI to flash the image on Windows and macOS.

<img src="https://i.imgur.com/W7YZc8j.png" width="450px" />

Next, insert the SD card, access the serial console with the baud rate settings `38400/8-N-1`, and then reset the board. It will boot the target from MMC and load `fw_dynamic.bin` and `u-boot.bin` from `u-boot.itb` in the first partition.
