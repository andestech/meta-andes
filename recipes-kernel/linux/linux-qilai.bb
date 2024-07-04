SUMMARY = "Andes Technology AndeSight 5.3.0 Linux v6.1"
inherit kernel
require recipes-kernel/linux/linux-yocto.inc
FILESEXTRAPATHS:prepend := "${THISDIR}/qilai:"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
KERNEL_VERSION_SANITY_SKIP = "1"

# 802aacbbffe2 (Linux 6.1.47)
SRCREV = "802aacbbffe2512dce9f8f33ad99d01cfec435de"
FORK = "andestech"
BRANCH = "ast-v5_3_0-branch"

SRC_URI = "git://github.com/${FORK}/linux.git;protocol=https;branch=${BRANCH} \
          "
SRC_URI:append:qilai = " \
    file://0001-riscv-andes-add-ae350-platform-to-SoC.patch \
    file://0002-riscv-andes-defconfig-add-andes_defconfig-and-andes-.patch \
    file://0003-riscv-andes-Add-Andes-specific-relocation-type.patch \
    file://0004-riscv-andes-add-new-readl_fixup-when-driver-probing.patch \
    file://0005-soc-andes-add-andes-platform-csr.h-and-proc.h-and-sb.patch \
    file://0006-riscv-andes-add-Andes-alternative-ports.patch \
    file://0007-riscv-andes-legacy_mmu-apply-errata-legacy-MMU-patch.patch \
    file://0008-soc-andes-cache-add-Andes-cache-support.patch \
    file://0009-riscv-andes-Implement-ioremap_wc-for-noncached-memor.patch \
    file://0010-riscv-andes-support-physical-address-MSB.patch \
    file://0011-soc-andes-ppma-support-Andes-PPMA-Programmable-Physi.patch \
    file://0012-soc-andes-dma-noncoherent-add-Andes-dma-noncoherent.patch \
    file://0013-dmaengine-andes-atcdmac300g-Andes-support-DMA-engine.patch \
    file://0014-mmc-andes-ftsdc010g-add-andes-ftsdc010g.patch \
    file://0015-mmc-andes-ftsdc010g-add-new-readl_fixup-when-driver-.patch \
    file://0016-net-andes-ftmac100-Andes-support-for-Faraday-ATCMAC.patch \
    file://0017-net-andes-ftmac100-add-new-readl_fixup-when-driver-p.patch \
    file://0018-RISC-V-mm-Support-huge-page-in-vmalloc_fault.patch \
    file://0019-riscv-andes-fix-ex_table-mismatch.patch \
    file://0020-soc-andes-cache-Remove-unused-l2c-functions.patch \
    file://0021-soc-andes-cache-remove-andes-userspace-cache-and-M-m.patch \
    file://0022-riscv-andes-defconfig-set-RISCV-SBI-earlycon-config-.patch \
    file://0023-riscv-Allow-to-downgrade-paging-mode-from-the-comman.patch \
    file://0024-riscv-Kconfig-Enable-cpufreq-kconfig-menu.patch \
    file://0025-riscv-andes-defconfig-enable-CONFIG_CPU_FREQ-for-and.patch \
    file://0026-soc-andes-ppma-Support-IPI-in-andes_free_ppma.patch \
    file://0027-soc-andes-injection-Andes-support-injection-feature.patch \
    file://0028-soc-andes-injection-upgrade-struct-file_operations-t.patch \
    file://0029-riscv-andes-dsp-DSP-support.patch \
    file://0030-rtc-andes-atcrtc100-Andes-support-for-ATCRTC.patch \
    file://0031-rtc-andes-atcrtc100-add-new-readl_fixup-when-driver-.patch \
    file://0032-soc-andes-ppma-fix-complier-error-when-disable-ppma.patch \
    file://0033-pwm-andes-atcpit100-add-Andes-s-PWM-module.patch \
    file://0034-i2c-andes-atciic100-add-Andes-s-I2C-module.patch \
    file://0035-dmaengine-andes-atcdmac300g-support-cyclic-mode.patch \
    file://0036-soc-andes-atcdmac300-Support-for-legacy-DMA-engine.patch \
    file://0037-mmc-andes-ftsdc010-support-legacy-MMC-driver.patch \
    file://0038-sound-andes-ftssp010-Support-for-audio-playback-thro.patch \
    file://0039-fbdev-andes-ftlcdc100-Update-kernel-configuration-to.patch \
    file://0040-soc-andes-atcdmac300-fix-channel-pending-when-burn-i.patch \
    file://0041-riscv-andes-defconfig-enable-CONFIG_HZ_100-for-andes.patch \
    file://0042-net-andes-ftmac100-use-casting-of-u64-to-avoid-overf.patch \
    file://0043-soc-andes-injection-injection-script-should-adhere-t.patch \
    file://0044-rtc-andes-atcrtc100-change-reads-the-RTC-four-times-.patch \
    file://0045-fbdev-andes-ftlcdc100-change-OSD_putc-if.else-to-swi.patch \
    file://0046-scripts-andes-headers_install.sh-Add-CONFIG_DSP-in-t.patch \
    file://0047-soc-andes-pm-Add-Andes-power-management-support.patch \
    file://0048-soc-andes-atcsmu-Add-ATCSMU-light-deep-sleep-support.patch \
    file://0049-watchdog-andes-atcwdt200-Andes-support-for-ATCWDT200.patch \
    file://0050-gpio-andes-atcgpio100-Support-Andes-ATCGPIO100.patch \
    file://0051-riscv-andes-defconfig-enable-ATCGPIO100-for-andes-su.patch \
    file://0052-spi-andes-atcspi200-Support-Andes-ATCSPI200.patch \
    file://0053-riscv-andes-defconfig-add-sv39.config-and-sv48.confi.patch \
    file://0054-v3PR-add-earlycon-sbi-to-extend-cmdline.patch \
    file://0055-riscv-andes-Kconfig-Disable-CONFIG_STRICT_KERNEL_RWX.patch \
    file://0056-v3PR-CONFIG_STRICT_KERNEL_RWX-n.patch \
    file://0057-riscv-andes-Add-Andes-specific-NORVC-relocation-type.patch \
    file://0058-fbdev-andes-ftlcdc100-Change-the-default-LCD-panel-t.patch \
    file://0059-net-andes-ftmac100-allow-setting-mac-address-by-devi.patch \
    file://0060-soc-andes-dma-noncoherent-select-ARCH_HAS_DMA_CLEAR_.patch \
    file://0061-riscv-dts-andes-ae350-Add-initial-Andes-ae350-device.patch \
    file://0062-fbdev-andes-ftlcdc100-To-support-AUA036QN01-change-t.patch \
    file://0063-i2c-andes-atciic100-Set-the-nr-to-1-to-dynamically-a.patch \
    file://0064-drivers-perf-riscv_pmu_sbi-add-support-for-PMU-varia.patch \
    file://0065-RISC-V-Cache-SBI-vendor-values.patch \
    file://0066-RISC-V-Create-unique-identification-for-SoC-PMU.patch \
    file://0067-drivers-perf-RISC-V-Allow-programming-custom-firmwar.patch \
    file://0068-riscv-andes-add-support-for-HPM-variant-on-Andes.patch \
    file://0069-riscv-andes-defconfig-add-config-and-include-for-And.patch \
    file://0070-riscv-andes-Fix-the-error-for-32-bit-perf.patch \
    file://0071-riscv-andes-add-Andes-AX45-JSON-file.patch \
    file://0072-tools-perf-andes-Add-Andes-L2C-events-to-JSON-files.patch \
    file://0073-perf-core-Implement-workaround-to-add-pmu-stop-befor.patch \
    file://0074-perf-core-Implement-workaround-to-fix-RCU-stall-issu.patch \
    file://0075-tool-perf-Adjust-the-sample-frequency-to-1000.patch \
    file://0076-tool-andes-perf-fix-perf-tools-compile-error-on-Linu.patch \
    file://0077-riscv-hwcap-Don-t-alphabetize-ISA-extension-IDs.patch \
    file://0078-riscv-Support-RISC-V-Svnapot-extension.patch \
    file://0079-posix_types.h-Add-workaround-fix-for-RV32-datatype-_.patch \
    file://0080-riscv-andes-trigger_module-Support-Andes-trigger_mod.patch \
    file://0081-riscv-dts-andes-Set-the-full-set-dts-for-each-core.patch \
    file://0082-riscv-andes-Support-Andes-supervisor-detailed-trap-c.patch \
    file://0083-riscv-amp-andes-add-andes_remoteproc-module.patch \
    file://0084-riscv-dts-andes-ae350-add-amp-device-tree-source-for.patch \
    file://0085-riscv-fpu-refine-FPU-save-flow.patch \
    file://0086-riscv-andes-Fix-WARN_ON-when-PROVE_LOCK-is-enabled.patch \
    file://0087-soc-andes-ppma-Replacing-smp_processor_id-with-get-p.patch \
    file://0088-gpio-andes-atcgpio100-Support-for-the-ngpios-variabl.patch \
    file://0089-riscv-Implement-flush_cache_vmap-and-add-a-call-to-f.patch \
    file://0090-clocksource-andes-atcpit-Support-Andes-ATCPIT-driver.patch \
    file://0091-clocksource-andes-atcpit-Refine-ATCPIT-driver.patch \
    file://0092-clocksource-andes-atcpit-Refine-the-source-code-of-t.patch \
    file://0093-perf-cpumap-Make-counter-as-unsigned-ints.patch \
    file://0094-riscv-andes-Add-AX45MPV-4-core-device-tree.patch \
    file://0095-riscv-Move-Andes-DSP-context-switch-out-of-__switch_.patch \
    file://0096-RISC-V-Improve-use-of-isa2hwcap.patch \
    file://0097-riscv-Rename-__switch_to_aux-fpu.patch \
    file://0098-riscv-Extending-cpufeature.c-to-detect-V-extension.patch \
    file://0099-riscv-Add-new-csr-defines-related-to-vector-extensio.patch \
    file://0100-riscv-Clear-vector-regfile-on-bootup.patch \
    file://0101-riscv-Disable-Vector-Instructions-for-kernel-itself.patch \
    file://0102-riscv-Introduce-Vector-enable-disable-helpers.patch \
    file://0103-riscv-Introduce-riscv_v_vsize-to-record-size-of-Vect.patch \
    file://0104-riscv-Introduce-struct-helpers-to-save-restore-per-t.patch \
    file://0105-riscv-Add-task-switch-support-for-vector.patch \
    file://0106-riscv-Allocate-user-s-vector-context-in-the-first-us.patch \
    file://0107-riscv-Add-ptrace-vector-support.patch \
    file://0108-riscv-signal-check-fp-reserved-words-unconditionally.patch \
    file://0109-riscv-signal-Add-sigcontext-save-restore-for-vector.patch \
    file://0110-riscv-signal-Report-signal-frame-size-to-userspace-v.patch \
    file://0111-riscv-signal-validate-altstack-to-reflect-Vector.patch \
    file://0112-riscv-prevent-stack-corruption-by-reserving-task_pt_.patch \
    file://0113-riscv-kvm-Add-V-extension-to-KVM-ISA.patch \
    file://0114-riscv-KVM-Add-vector-lazy-save-restore-support.patch \
    file://0115-riscv-hwcap-change-ELF_HWCAP-to-a-function.patch \
    file://0116-riscv-Add-prctl-controls-for-userspace-vector-manage.patch \
    file://0117-riscv-Add-sysctl-to-set-the-default-vector-rule-for-.patch \
    file://0118-riscv-detect-assembler-support-for-.option-arch.patch \
    file://0119-riscv-Enable-Vector-code-to-be-built.patch \
    file://0120-riscv-Add-documentation-for-Vector.patch \
    file://0121-RISC-V-Remove-ptrace-support-for-vectors.patch \
    file://0122-RISC-V-vector-export-VLENB-csr-in-__sc_riscv_v_state.patch \
    file://0123-riscv-vector-clear-V-reg-in-the-first-use-trap.patch \
    file://0124-sched-fair-Fix-cfs_rq_is_decayed-on-SMP.patch \
    file://0125-andes-perf-add-andes-event-to-JSON-file.patch \
    file://0126-riscv-andes-defconfig-andes_defconfig-default-disabl.patch \
    file://0127-riscv-andes-Add-RECOVER_UPSTREAM_CONFIG_RISCV-to-sup.patch \
    file://0128-riscv-andes-fix-make-failure-with-riscv-generic-defc.patch \
    file://0129-cpufreq-andes-cpufreq-Andes-processors-PowerBrake-dr.patch \
    file://0130-riscv-andes-remove-config-ANDES_QEMU_SUPPORT.patch \
    file://0131-gpio-andes-atcgpio100-Fix-recursive-deadlock-when-se.patch \
    file://0132-dmaengine-andes-atcdmac300g-Fix-some-typos.patch \
    file://0133-clocksource-andes-atcpit-Refine-the-source-code-of-t.patch \
    file://0134-andes-ftsdc010-Add-another-revision-number.patch \
    file://0135-riscv-dts-andes-fix-device-node-interrupt-parent.patch \
    file://0136-riscv-signal-fix-sigaltstack-frame-size-checking.patch \
    file://0137-RISC-V-Add-ptrace-support-for-vectors.patch \
    file://0138-input-andes-touchscreen-Add-the-touch-screen-driver-.patch \
    file://0139-input-andes-touchscreen-Refine-the-driver.patch \
    file://0140-riscv-andes-mm-Synchronize-memory-attributes-for-all.patch \
    file://0141-riscv-andes-defconfig-enable-CONFIG_PREEMPT-allows-f.patch \
    file://0142-riscv-andes-a25mp-Add-preempt-disable-enable-pair-to.patch \
    file://0143-andes-perf-Fix-event-encoding-of-0x32-on-25-and-65-s.patch \
    file://0144-andes-perf-Add-27-series-event-encoding.patch \
    file://0145-scripts-gdb-fix-SB_-constants-parsing.patch \
    file://0146-kselftest-Increase-timeout-value-for-andes-platform.patch \
    file://0147-selftests-exec-Change-the-command-interpreter-of-the.patch \
    file://0148-selftests-lkdtm-Remove-the-testcase-that-causes-the-.patch \
    file://0149-selftests-Remove-some-unsupported-commands-by-Busybo.patch \
    file://0150-selftests-filesystems-Add-six-consecutive-x-characte.patch \
    file://0151-selftests-lib.mk-Remove-rsync-L-option.patch \
    file://0152-selftest-zram-Specify-block-size-as-4096-when-mkfs-o.patch \
    file://0153-selftests-kselftest_harness.h-Increase-default-timeo.patch \
    file://0154-Makefile-Exclude-these-test-cases-when-kselftest-mer.patch \
    file://0155-selftests-ptrace-Remove-the-vmaccess-test-case.patch \
    file://0156-selftests-Makefile-Add-some-test-to-SKIP_TARGETS.patch \
    file://0157-selftests-Enable-some-required-kernel-config.patch \
    file://0158-drivers-soc-andes-Add-the-print_detailed_cause-metho.patch \
    file://0159-drivers-soc-andes-Register-the-L2-cache-IRQ.patch \
    file://0160-drivers-soc-andes-Fix-an-error-for-print_detailed_ca.patch \
    file://0161-IIC-andes-atciic100-Pass-slave-device-DTS-settings-t.patch \
    file://0162-qilai-andes-support-andes-qilai-soc-platform.patch \
    file://0163-inject-qilai-andes-add-offset-for-inject-script.patch \
    file://0164-dma-iocp-qilai-add-dma-and-iocp-support-for-qilai.patch \
    file://0165-workaround-Reserved-3-PMA-entry-for-early-seeting-in.patch \
    file://0166-i2c-andes-atci2c100-Fix-the-incorrect-timiing-settin.patch \
    file://0167-workaround-skip-swiotlb-for-Qilai.patch \
    file://0168-Merge-the-Qilai-PCIe-from-linux-5.4.patch \
    file://0169-qilai-pcie-add-pcie-device-support.patch \
    file://0170-dma-andes-fix-type-on-CACHE_CTRL.patch \
    file://0171-qilai-qilai-support-OpenAMP.patch \
    file://0172-sound-andes-ftssp-Support-ftssp010-driver.patch \
    file://0173-add-openamp-into-qilai_gpu.dts.patch \
    file://0174-remove-unnecessary-config.patch \
    file://0175-renemae-SOC_RESET_VECTOR-to-SMU_RESET_VECTOR.patch \
    file://0176-pcie-axcache-enable-axcache.patch \
    file://0177-drivers-USB-support-for-Renesas-PCI-to-USB.patch \
    file://0178-rtc-disable-the-on-cpu-rtc-atcrtc100.patch \
    file://0179-configs-demo-support-demo-defconfig.patch \
    file://0180-sound-andes-ftssp-Refine-source-code-to-meet-coding-.patch \
    file://0181-configs-openamp-fix-typo-on-file-name.patch \
    file://0182-RISC-V-Provide-pgtable_l5_enabled-on-rv32.patch \
    file://0183-riscv-andes-add-GPL-to-readl_fixup-EXPORT_SYMBOL.patch \
    file://0184-spi-andes-atcspi200-Register-the-spi-interrupt.patch \
    file://0185-spi-andes-atcspi200-spi-support-ads7846-touchscreen.patch \
    file://0186-spi-andes-atcspi200-supports-quad-read-write-mode.patch \
    file://0187-rtc-andes-atcrtc100-Use-the-native-Linux-IO-APIs-to-.patch \
    file://0188-rtc-andes-atcrtc100-handle-the-situation-where-the-s.patch \
    file://0189-rtc-andes-atcrtc100-Support-range_min-and-range_max-.patch \
    file://0190-drivers-soc-andes-fix-cpu_dcache_-wb-inval-_range-fa.patch \
    file://0191-drivers-i2c-andes-using-subsys_initcall.patch \
    file://qilai_rv64_smp_demo_defconfig \
"

LINUX_VERSION ?= "v6.1.47"
LINUX_VERSION_EXTENSION:append = "-qilai"

PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE = "qilai"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"
KCONF_AUDIT_LEVEL = "2"
