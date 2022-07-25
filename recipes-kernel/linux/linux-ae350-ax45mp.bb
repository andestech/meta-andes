FILESEXTRAPATHS =. "${FILE_DIRNAME}/files/ae350-ax45mp:"
SUMMARY = "Andes ast-v5_2_0 Linux 5.4"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

BRANCH = "RISCV-Linux-5.4-ast-v5_2_0-branch"
SRC_URI = "git://${LOCAL_SRC}/linux-5.4;protocol=file;branch=${BRANCH} \
           file://linux.cfg \
          "

SRCREV = "6b498239e5dd1a645172a6c806bec174341599d2"

PV = "5.4.192"

KCONFIG_MODE = "--alldefconfig"
KBUILD_DEFCONFIG:ae350-ax45mp = "ae350_rv64_smp_defconfig"

COMPATIBLE_MACHINE = "(ae350-ax45mp)"
