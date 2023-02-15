require linux-mainline-common.inc
FILESEXTRAPATHS =. "${FILE_DIRNAME}/files:"
SUMMARY = "Andes ast-v5_2_0 Linux 5.4"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

KERNEL_VERSION_SANITY_SKIP = "1"

SRCREV = "f1d0d3f652787c0e9dda97ea12a954a38397834d"
FORK = "andestech"
BRANCH = "RISCV-Linux-5.4-ast-v5_2_0-branch"
SRC_URI = "git://github.com/${FORK}/linux.git;protocol=https;branch=${BRANCH} \
           file://0001-riscv-dts-add-andes-folder.patch \
           file://linux.cfg \
           file://rtc-support.cfg \
           file://wdt-support.cfg \
          "

LINUX_VERSION ?= "v5.4.220"
LINUX_VERSION_EXTENSION:append:ae350-ax45mp = "-ae350"

KBUILD_DEFCONFIG:ae350-ax45mp = "ae350_rv64_smp_defconfig"

COMPATIBLE_MACHINE = "(ae350-ax45mp)"
