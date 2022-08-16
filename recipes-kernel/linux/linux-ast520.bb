require linux-mainline-common.inc
FILESEXTRAPATHS =. "${FILE_DIRNAME}/files:"
SUMMARY = "Andes ast-v5_2_0 Linux 5.4"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

KERNEL_VERSION_SANITY_SKIP = "1"

SRCREV = "9e68c34e2c4013ac74dc450227c7e3efdb6ce96f"
BRANCH = "RISCV-Linux-5.4-ast-v5_2_0-branch"
SRC_URI = "git:///work/linux-5.4;protocol=file;branch=${BRANCH} \
           file://0001-riscv-dts-add-andes-folder.patch \
           file://linux.cfg \
           file://rtc-support.cfg \
           file://wdt-support.cfg \
          "

LINUX_VERSION ?= "v5.4.192"
LINUX_VERSION_EXTENSION:append:ae350-ax45mp = "-ae350"

KBUILD_DEFCONFIG:ae350-ax45mp = "ae350_rv64_smp_defconfig"

COMPATIBLE_MACHINE = "(ae350-ax45mp)"
