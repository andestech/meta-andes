SUMMARY = "RISC-V Open Source Supervisor Binary Interface (OpenSBI)"
DESCRIPTION = "OpenSBI aims to provide an open-source and extensible implementation of the RISC-V SBI specification for a platform specific firmware (M-mode) and a general purpose OS, hypervisor or bootloader (S-mode or HS-mode). OpenSBI implementation can be easily extended by RISC-V platform or System-on-Chip vendors to fit a particular hadware configuration."
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD;md5=42dd9555eb177f35150cf9aa240b61e5"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit autotools-brokensep deploy

PV = "1.0+git${SRCPV}"

FORK = "andestech"
BRANCH = "opensbi-ast-v5_2_0-branch"
SRCREV = "3e46fc7b075c2a57568600ccf6cd04d31e8918df"
SRC_URI = "git://github.com/${FORK}/opensbi.git;protocol=https;branch=${BRANCH} \
           file://0001-force-flush-pipeline-for-outstanding-memory-access.patch \
          "

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "PLATFORM=${RISCV_SBI_PLAT} I=${D} INSTALL_LIB_PATH=lib"

do_install:append() {
	# In the future these might be required as a dependency for other packages.
	# At the moment just delete them to avoid warnings
	find ${D}
	rm -r ${D}/include
	rm -r ${D}/lib
}

do_deploy () {
	install -m 755 ${D}/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_dynamic.* ${DEPLOYDIR}/
}

addtask deploy before do_build after do_install

FILES:${PN} += "/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_dynamic.*"

COMPATIBLE_HOST = "(riscv64|riscv32).*"
INHIBIT_PACKAGE_STRIP = "1"

INSANE_SKIP:${PN}:ae350-ax45mp += "ldflags"
