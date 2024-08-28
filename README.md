# Andes OpenEmbedded/Yocto Layer

This layer provides machine configurations and recipes for building the bootable disk image with AndeSight™ Linux packages.

## Supported AndesCore™ Processors

- [x] [ae350-a25mp](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-a25mp/)
- [x] [ae350-a27l2](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-a27l2/)
- [x] [ae350-a45mp](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-a45mp/)
- [x] [ae350-ax25mp](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax25mp/)
- [x] [ae350-ax27l2](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax27l2/)
- [x] [ae350-ax45mp](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax45mp/)
- [x] [ae350-ax45mpv](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax45mpv/)
- [x] [ae350-ax65](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax65/)

## Building SD Card Image with kas-container

[kas-container](https://kas.readthedocs.io/en/4.1/userguide.html) provides a Yocto development environment based on the Debian docker image. Before you proceed with the build process, make sure Docker is installed on your host machine.

```
$ mkdir riscv-andes && cd riscv-andes
$ git clone https://github.com/andestech/meta-andes.git -b ast-v5_3_0-branch
$ wget https://raw.githubusercontent.com/siemens/kas/4.1/kas-container
$ chmod a+x ./kas-container
```

AndeSight™ v5.3.0 includes OpenSBI, U-Boot and Linux source based on the following versions.

* [OpenSBI v1.2](https://github.com/andestech/opensbi/tree/ast-v5_3_0-branch)
* [U-Boot v2023.01](https://github.com/andestech/uboot/tree/ast-v5_3_0-branch)
* [Linux 6.1.47](https://github.com/andestech/linux/tree/ast-v5_3_0-branch)

And, its RISC-V GNU toolchain versions are as follows:

* GCC 13.2.0
* Binutils 2.41

To build a Poky reference distribution, take `ae350-ax45mp` as an example:

```
$ ./kas-container build meta-andes/kas/ae350-ax45mp.yml
```

### Build Results

Find the built image, bootloader binaries and boot files generated in **build/tmp/deploy/images/<MACHINE>**, such as

* core-image-base-ae350-ax45mp.rootfs.wic.gz
* fitImage
* ax45mp_c4_d_dsp_ae350.dtb
* boot.scr.uimg
* uEnv.txt
* u-boot-spl.bin
* u-boot.itb

## Updating U-Boot SPL, U-Boot ITB and Device Tree on Flash

To update the bootloader, use the [SPI_Burn](https://github.com/andestech/Andes-Development-Kit) tool.
Ensure you have an ICEman connection set up as follows:

```
  Local Host                 Local/Remote Host
 .----------------.          .--------------.
 | yocto images   |          |              |
 |                |         ICEman host <IP:PORT>
 | .----------.   |          |  .--------.  |
 | | SPI_burn |<--+--socket--+->| ICEman |  |
 | '----------'   |          |  '--.-----'  |
 '----------------'          '-----|--------'
                                   |
                                   USB
   .--------------.                |
   | target       |          .-----v-----.
   | board        <---JTAG---| ICE       |
   |              |          '-----------'
   '--------------'
```

Download & extract `SPI_burn`:

```
$ wget https://github.com/andestech/Andes-Development-Kit/releases/download/ast-v5_3_0-release-windows/flash.zip
$ unzip flash.zip
$ cd ./flash/src-SPI_burn
```

Build `SPI_burn`:

```
$ ./build_SPIburn.sh
```

Program the U-Boot SPL & ITB and device-tree blob onto flash memory:

```
$ ICE_HOST=<ICEman host IP>
$ ICE_PORT=<ICEman host port> # Note that this is the "Burner port"
$ ./SPI_burn --host $ICE_HOST --port $ICE_PORT --addr 0x0 -i u-boot-spl.bin
$ ./SPI_burn --host $ICE_HOST --port $ICE_PORT --addr 0x10000 -i u-boot.itb
$ ./SPI_burn --host $ICE_HOST --port $ICE_PORT --addr 0xf0000 -i ae350.dtb
```

## Flashing Image to SD Card

Use the Linux `dd` command to flash the image to an SD card.

```
$ gunzip -c <IMAGE>.wic.gz | sudo dd of=/dev/sdX bs=4M iflag=fullblock oflag=direct conv=fsync status=progress
$ sync
```

You can also use the [belenaEther](https://www.balena.io/etcher/) GUI to flash the image on Windows and macOS.

<img src="https://i.imgur.com/W7YZc8j.png" width="450px" />

Upon inserting the SD card, access the serial console (e.g. [`picocom`](https://linux.die.net/man/8/picocom)) with the baud rate settings `38400/8-N-1`, and then reset the board, the system should start the boot process.

```
$ sudo picocom -b 38400 /dev/ttyUSB1
```

### RISC-V Boot Process

Andes platforms follow the typical RISC-V boot process illustrated below.

```
                       .-------------------------.
                       | (u-boot.itb)            |
                       |                         |
(1)-----------------+  | (2)-----------------+   |
 |  U-Boot SPL      |--+->|  OpenSBI         |---|
 | (u-boot-spl.bin) |  |  | (fw_dynamic.bin) |   |
 +------------------+  |  +---------.--------+   |
                       |            |            |
Machine mode           |            |            |
.......................|............|............|..................
Supervisor mode        |            |            |
                       |            v            |
                       | (3)-------------------+ |   (4)-----------+
                       |  |  U-Boot            |-+--->|  Linux     |
                       |  | (u-boot-nodtb.bin) | |    | (fitImage) |
                       |  +--------------------+ |    +------------+
                       '-------------------------'
```

## (Optional.) Updating U-Boot SPL, U-Boot ITB and Device Tree via U-Boot `sf` command

The U-Boot [`sf`](https://docs.u-boot.org/en/latest/usage/cmd/sf.html) command provides an alternative method for updating flash memory.
Upon flashing the `<IMAGE>.wic.gz` file to an SD card, the U-Boot SPL (`u-boot-spl.bin`), ITB (`u-boot.itb`), and the device-tree blob are subsequently located in the first partition.

```
RISC-V # fatload mmc 0:1 0x600000 u-boot-spl.bin
RISC-V # sf probe 0:0 50000000 0
RISC-V # sf erase 0x0 0x10000
RISC-V # sf write 0x600000 0x0 0x10000

RISC-V # fatload mmc 0:1 0x600000 u-boot.itb
RISC-V # sf probe 0:0 50000000 0
RISC-V # sf erase 0x10000 0xa0000
RISC-V # sf write 0x600000 0x10000 0xa0000

RISC-V # fatload mmc 0:1 0x20000000 ae350.dtb
RISC-V # sf probe 0:0 50000000 0
RISC-V # sf erase 0xf0000 0x10000
RISC-V # sf write 0x20000000 0xf0000 0x10000
```

### Resetting the Board via GDB

Set `<TARGET_IP>` to the IP address of the ICEman host.

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
