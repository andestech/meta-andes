SUMMARY = "Andes Technology AndeSight 5.4.0 Linux v6.6"
inherit kernel
require recipes-kernel/linux/linux-yocto.inc
FILESEXTRAPATHS =. "${FILE_DIRNAME}/files:"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
KERNEL_VERSION_SANITY_SKIP = "1"
# Tag: ast-v5_4_2-release (2026/4/13)
SRCREV = "7908d1aed3dbeea4173b92f90d67cf300d29f633"
FORK = "andestech"
BRANCH = "ast-v5_4_0-branch"

SRC_URI:riscv64 = " \
    git://github.com/${FORK}/linux.git;protocol=https;branch=${BRANCH} \
    file://ae350_rv64_smp_defconfig \
    file://tweak.cfg \
"

SRC_URI:riscv32 = " \
    git://github.com/${FORK}/linux.git;protocol=https;branch=${BRANCH} \
    file://ae350_rv32_smp_defconfig \
    file://tweak.cfg \
"

LINUX_VERSION ?= "v6.6.49"
LINUX_VERSION_EXTENSION:append = "-ae350"

PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE = "(ae350-*)"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"
KCONF_AUDIT_LEVEL = "2"
