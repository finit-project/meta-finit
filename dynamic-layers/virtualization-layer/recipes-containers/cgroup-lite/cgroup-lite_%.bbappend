inherit finit

FINIT_SERVICES:${PN} = "cgroups-mount"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'finit', 'true', 'false', d)}; then
        sed -i -e "s#systemd#${INIT_MANAGER}#g" ${D}${base_bindir}/cgroups-mount
    fi
}
