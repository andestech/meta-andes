#@DESCRIPTION: Machine configuration for the Andes platforms
#@MAINTAINER: Ben Zong-You Xie <ben717@andestech.com>

require conf/machine/include/riscv/tune-riscv.inc

#============================================
# Common Linux Kernel Configuration
KERNEL_CLASSES = "kernel-fitimage"
KERNEL_IMAGETYPE = "fitImage"
#============================================

#============================================
# Common Hardware Configuration
MACHINE_FEATURES = "screen keyboard ext2 ext3 serial"
SERIAL_CONSOLES = "115200;ttyS0"
MACHINE_EXTRA_RRECOMMENDS += " kernel-modules"
#============================================

#============================================
# Preferred Providers
PREFERRED_PROVIDER_virtual/kernel ?= "linux-ast540"
PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-ast540"
#============================================

#============================================
# SBI Configuration
RISCV_SBI_PLAT = "generic"
RISCV_SBI_PAYLOAD ?= "u-boot.bin"
#============================================

#============================================
# Common Image Configuration
EXTRA_IMAGEDEPENDS += "opensbi-ast540"
EXTRA_IMAGEDEPENDS += "u-boot-ast540"
WKS_FILE_DEPENDS ?= " \
    u-boot-ast540 \
    opensbi-ast540 \
    e2fsprogs-native \
    bmaptool-native \
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
