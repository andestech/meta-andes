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
- [x] [ae350-ax46mpv](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax46mpv/)
- [x] [ae350-ax65](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax65/)
- [x] [ae350-ax66](https://www.andestech.com/en/products-solutions/andescore-processors/riscv-ax66/)

## Building SD Card Image with kas-container

[kas-container](https://kas.readthedocs.io/en/4.1/userguide.html) provides a Yocto development environment based on the Debian docker image. Before you proceed with the build process, make sure Docker is installed on your host machine.

```
$ mkdir riscv-andes && cd riscv-andes
$ git clone https://github.com/andestech/meta-andes.git -b ast-v5_4_0-branch
$ wget https://raw.githubusercontent.com/siemens/kas/4.1/kas-container
$ chmod a+x ./kas-container
```

AndeSight™ v5.4.0 includes OpenSBI, U-Boot and Linux source based on the following versions.

* [OpenSBI v1.5.1](https://github.com/andestech/opensbi/tree/ast-v5_4_0-branch)
* [U-Boot v2024.07](https://github.com/andestech/uboot/tree/ast-v5_4_0-branch)
* [Linux 6.6.49](https://github.com/andestech/linux/tree/ast-v5_4_0-branch)

And, its RISC-V GNU toolchain versions are as follows:

* GCC 13.2.0
* Binutils 2.42

To build a Poky reference distribution, take `ae350-ax45mp` as an example:

```
$ ./kas-container build meta-andes/kas/ae350-ax45mp.yml
```

### Build Results

Upon completion of the build process, the build results are located within the directory `build/tmp/deploy/images/ae350-ax45mp/`.

| File                            | Description                                                                                      |
|---------------------------------|--------------------------------------------------------------------------------------------------|
| `core-image-base-ae350-ax45mp.rootfs.wic.gz` | A compressed root filesystem image which will be written onto the SD card. |
| `fitImage`                    | A Flattened Image Tree (FIT) that contains the Linux kernel and device-tree with their load address. |
| `ax45mp_c4_d_dsp_ae350.dtb`     | Device Tree Blob generated using the `ae350-ax45mp` YAML file. The naming convention is structured as follows: `<cpu>_c<core-count>_<double-float-support>_<andes-dsp-support>_<platform>.dtb`. It will be programmed onto flash memory using `SPI_burn` tool. |
| [`boot.scr.uimg`](https://github.com/andestech/meta-andes/blob/ast-v5_4_0-branch/recipes-bsp/u-boot/files/tftp-mmc-boot.txt)                 | U-Boot script image, automating the boot process by attempting various boot scenarios such as loading boot files via TFTP or MMC, and executing the corresponding commands to load and boot the kernel images. |
| [`uEnv.txt`](https://github.com/andestech/meta-andes/blob/ast-v5_4_0-branch/recipes-bsp/u-boot/files/uEnv.txt)                      | Contains boot environment variables for U-Boot, specifying parameters like boot arguments, and commands to load and run kernel images (e.g. `fitImage`). |
| `u-boot-spl.bin`                | The Secondary Program Loader (SPL) of U-Boot which will be programmed onto flash memory using `SPI_burn` tool. |
| `u-boot.itb`                    | An image tree binary of U-Boot, combining OpenSBI (fw_dynamic.bin) which will be loaded by U-Boot SPL. It will be programmed onto flash memory using `SPI_burn` tool.|

## Updating U-Boot SPL, U-Boot ITB and Device Tree on Flash

To update the bootloader, use the [SPI_burn](https://github.com/andestech/Andes-Development-Kit) tool.
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

> You can download the [pre-built SPI_burn](https://github.com/andestech/meta-andes/raw/ast-v5_3_0-branch/tools/SPI_burn) for x86 hosts and skip building it locally from source.

Download & extract `SPI_burn` source code:

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
$ ICE_PORT=<ICEman host burner port>
$ ./SPI_burn --host $ICE_HOST --port $ICE_PORT --addr 0x0 -i u-boot-spl.bin
$ ./SPI_burn --host $ICE_HOST --port $ICE_PORT --addr 0x40000 -i u-boot.itb
$ ./SPI_burn --host $ICE_HOST --port $ICE_PORT --addr 0x1E0000 -i ax45mp_c4_d_dsp_ae350.dtb
```

## Flashing Image to SD Card

Use the Linux `dd` command to flash the image to an SD card.

```
$ gunzip -c <IMAGE>.wic.gz | sudo dd of=/dev/sdX bs=4M iflag=fullblock oflag=direct conv=fsync status=progress
$ sync
```

You can also use the [balenaEther](https://www.balena.io/etcher/) GUI to flash the image on Windows and macOS.

<img src="https://i.imgur.com/W7YZc8j.png" width="450px" />

Upon inserting the SD card, access the serial console (e.g. [`picocom`](https://linux.die.net/man/8/picocom)) with the baud rate settings `115200/8-N-1`, and then reset the board, the system should start the boot process.

```
$ sudo picocom -b 115200 /dev/ttyUSB1
```

### RISC-V Boot Process

Andes platforms follow the typical RISC-V boot process illustrated below.

```
                       .-------------------------.
                       | (u-boot.itb)            |
                       |                         |
(1)-----------------+  | (2)-----------------+   |
 |  U-Boot SPL      |--+->|  OpenSBI         |   |
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

```
$ ICE_HOST=<ICEman host IP>
$ ICE_PORT=<ICEman host debug port>
$ $GDB -ex "target remote $ICE_HOST:$ICE_PORT" \
       -ex "set confirm off" \
       -ex "set pagination off" \
       -ex "monitor reset halt" \
       -ex "set \$ra=0" \
       -ex "set \$sp=0" \
       -ex "maintenance flush register-cache" \
       -ex "c"
```
