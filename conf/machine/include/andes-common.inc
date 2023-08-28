#@DESCRIPTION: Machine configuration for the Andes platforms
#@MAINTAINER: Yu Chien Peter Lin <peterlin@andestech.com>

require conf/machine/include/riscv/tune-riscv.inc

#============================================
# Common Linux Kernel Configuration
KERNEL_CLASSES = "kernel-fitimage"
KERNEL_IMAGETYPE = "fitImage"
#============================================

#============================================
# Common Hardware Configuration
MACHINE_FEATURES = "screen keyboard ext2 ext3 serial"
SERIAL_CONSOLES = "38400;ttyS0"
MACHINE_EXTRA_RRECOMMENDS += " kernel-modules"
#============================================

#============================================
# Preferred Providers
PREFERRED_PROVIDER_virtual/kernel ?= "linux-ast530"
PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-ast530"
#============================================

#============================================
# SBI Configuration
RISCV_SBI_PLAT = "generic"
RISCV_SBI_PAYLOAD ?= "u-boot.bin"
#============================================

#============================================
# Common Image Configuration
EXTRA_IMAGEDEPENDS += "opensbi-ast530"
EXTRA_IMAGEDEPENDS += "u-boot-ast530"
WKS_FILE_DEPENDS ?= " \
    u-boot-ast530 \
    opensbi-ast530 \
    e2fsprogs-native \
    bmap-tools-native \
"
IMAGE_FSTYPES += "wic.gz wic.bmap ext4"
IMAGE_BOOT_FILES ?= " \
    ${KERNEL_IMAGETYPE} \
    ${RISCV_SBI_FDT} \
    boot.scr.uimg \
    uEnv.txt \
    u-boot.itb \
    u-boot-spl.bin \
"
WKS_FILE ?= "andes-ae350-spl.wks"
#============================================
