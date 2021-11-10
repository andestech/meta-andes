FILESEXTRAPATHS =. "${FILE_DIRNAME}/files/ae350-ax45mp:"
SUMMARY = "Andes ast-v5_0_0 Linux 5.4"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

BRANCH = "RISCV-Linux-5.4-ast-v5_0_0-branch"
SRC_URI = "git://${LOCAL_SRC}/linux-5.4;protocol=file;branch=${BRANCH} \
           file://0001-Add-custom-CSR.patch \
           file://linux.cfg \
          "

SRCREV = "ec7e3bd0b86b31d423ed305a6f137b288bdc1bf2"
LINUX_VERSION = "5.4"
LINUX_VERSION_EXTENSION:append = "-ast500"


PV = "${LINUX_VERSION}+git${SRCPV}"

KCONFIG_MODE = "--alldefconfig"
KBUILD_DEFCONFIG:ae350-ax45mp = "ae350_rv64_smp_defconfig"

COMPATIBLE_MACHINE = "(ae350-ax45mp)"
