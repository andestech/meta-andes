# Andes OpenEmbedded/Yocto Layer

This layer provides `ae350-ax45mp` machine configuration and recipes for building the bootable disk image with AndeSight Linux packages.

## Building SD card image with kas-container

[kas-container](https://kas.readthedocs.io/en/3.0.2/userguide.html) is used to set up an out of the box Yocto development environment based on the Debian docker image; before proceeding, make sure you have Docker installed on your host machine.

```
$ mkdir riscv-andes && cd riscv-andes
$ git clone https://github.com/andestech/meta-andes.git -b ast-v5_2_0-branch
$ wget https://raw.githubusercontent.com/siemens/kas/3.0.2/kas-container
$ chmod a+x ./kas-container
```

AndeSight 5.2.0 provides OpenSBI, U-boot and Linux based on the following version.

* [OpenSBI v1.0](https://github.com/andestech/opensbi/tree/opensbi-ast-v5_2_0-branch)
* [U-boot v2022.04](https://github.com/andestech/uboot/tree/ast-v5_2_0-branch)
* [Linux 5.4.220](https://github.com/andestech/linux/tree/RISCV-Linux-5.4-ast-v5_2_0-branch)

Build poky distro:

```
$ ./kas-container build meta-andes/ae350-ax45mp_poky.yml
```

Build nodistro:

```
$ ./kas-container build meta-andes/ae350-ax45mp.yml
```

### Build results

The generated SD card image will be located in the **build/tmp-glibc/deploy/images/<MACHINE>** directory (or **build/tmp/deploy/images/<MACHINE>** for poky distro).

* core-image-minimal-ae350-ax45mp.wic.gz
* fitImage
* ae350_c4_64_d.dtb
* boot.scr.uimg
* uEnv.txt
* u-boot-spl.bin
* u-boot.itb

## Flashing Image to SD Card

Use Linux `dd` command to flash the image.

```
$ gunzip -c <IMAGE>.wic.gz | sudo dd of=/dev/sdX bs=4M iflag=fullblock oflag=direct conv=fsync status=progress && sync
```

On Windows and macOS, [belenaEther](https://www.balena.io/etcher/) provides GUI to flash the image.

<img src="https://i.imgur.com/W7YZc8j.png" width="450px" />

Insert SD card and access serial console with baud `38400/8-N-1`, then reset the board, it should boot from MMC and loads `fw_dynamic.bin` and `u-boot.bin` from `u-boot.itb` located at first partition.

## (Optional.) Flashing U-boot SPL

If you need to update the SPL, the first partition holds XIP mode SPL (u-boot-spl.bin) that can be burned on flash by using the `sf` command shown below in U-boot prompt or [SPI_Burn tool](https://github.com/andestech/Andes-Development-Kit).

```
RISC-V # fatload mmc 0:1 0x600000 u-boot-spl.bin
RISC-V # sf probe 0:0 50000000 0
RISC-V # sf erase 0x0 0x10000
RISC-V # sf write 0x600000 0x0 0x10000
```

### Reset the board via GDB

Set `<TARGET_IP>` to the ICEman host IP address.

```
$ $GDB -ex "target remote <TARGET_IP>:<PORT>" \
       -ex "set confirm off" \
       -ex "set pagination off" \
       -ex "monitor reset halt" \
       -ex "set \$ra=0" \
       -ex "set \$sp=0" \
       -ex "flushregs" \
       -ex "c"
```
