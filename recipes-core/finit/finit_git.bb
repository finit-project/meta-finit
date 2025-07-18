SUMMARY = "Fast init for Linux systems"
DESCRIPTION = "Fast init for Linux systems"
SECTION = "base"
HOMEPAGE = "https://github.com/troglobit/finit"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7f4881796913b7fa3c08182acd0b3987"

PACKAGE_ARCH = "${MACHINE_ARCH}"

def get_custom_rtc_restore_date(d):
    import datetime
    return datetime.datetime.fromtimestamp(int(d.getVar("REPRODUCIBLE_TIMESTAMP_ROOTFS")), datetime.timezone.utc).strftime('%4Y-%2m-%2d %2H:%2M:%2S')

RTC_RESTORE_DATE = "${@get_custom_rtc_restore_date(d)}"

PACKAGECONFIG ??= "auto-reload \
                   fastboot \
                   random-seed \
                   hook-scripts-plugin \
                   kernel-cmdline \
                   libsystemd \
                   modules-load-plugin \
                   hotplug-plugin \
                   netlink-plugin \
                   dbus-plugin \
                   rtc-plugin \
                   urandom-plugin \
                   redirect \
                   rescue \
                   tty-plugin \
                  "

PACKAGECONFIG[random-seed] = "--with-random-seed=/var/lib/misc/random-seed,--without-random-seed"
PACKAGECONFIG[auto-reload] = "--enable-auto-reload,--disable-auto-reload"
PACKAGECONFIG[cgroup] = "--enable-cgroup,--disable-cgroup"
PACKAGECONFIG[contrib] = "--enable-contrib,--disable-contrib"
PACKAGECONFIG[doc] = "--enable-doc,--disable-doc"
PACKAGECONFIG[kernel-cmdline] = "--enable-kernel-cmdline,--disable-kernel-cmdline"
PACKAGECONFIG[kernel-logging] = "--enable-kernel-logging,--disable-kernel-logging"
PACKAGECONFIG[fastboot] = "--enable-fastboot,--disable-fastboot"
PACKAGECONFIG[fsckfix] = "--enable-fsckfix,--disable-fsckfix"
PACKAGECONFIG[redirect] = "--enable-redirect,--disable-redirect"
PACKAGECONFIG[rescue] = "--enable-rescue,--disable-rescue"
PACKAGECONFIG[libsystemd] = "--with-libsystemd,--without-libsystemd"
PACKAGECONFIG[sulogin] = "--with-sulogin,--without-sulogin,,util-linux-sulogin"
PACKAGECONFIG[modules-load-plugin] = "--enable-modules-load-plugin,--disable-modules-load-plugin"
PACKAGECONFIG[modprobe-plugin] = "--enable-modprobe-plugin,--disable-modprobe-plugin,,kmod"
PACKAGECONFIG[hotplug-plugin] = "--enable-hotplug-plugin,--disable-hotplug-plugin,udev,udev"
PACKAGECONFIG[hook-scripts-plugin] = "--enable-hook-scripts-plugin,--disable-hook-scripts-plugin"
PACKAGECONFIG[rc-local] = "--with-rc-local=${sysconfdir}/rc.loal,--without-rc-local"
PACKAGECONFIG[rtc-plugin] = '--enable-rtc-plugin --with-rtc-date="${RTC_RESTORE_DATE}",--disable-rtc-plugin'
PACKAGECONFIG[urandom-plugin] = "--enable-urandom-plugin,--disable-urandom-plugin"
PACKAGECONFIG[tty-plugin] = "--enable-tty-plugin,--disable-tty-plugin"
PACKAGECONFIG[netlink-plugin] = "--enable-netlink-plugin,--disable-netlink-plugin"
PACKAGECONFIG[dbus-plugin] = "--enable-dbus-plugin,--disable-dbus-plugin,dbus,dbus"
PACKAGECONFIG[alsa-utils-plugin] = "--enable-alsa-utils-plugin,--disable-alsa-utils-plugin,alsa-utils,alsa-utils-alsactl"
PACKAGECONFIG[x11-common-plugin] = "--enable-x11-common-plugin,--disable-x11-common-plugin,virtual/libx11"
PACKAGECONFIG[resolvconf-plugin] = "--enable-resolvconf-plugin,--disable-resolvconf-plugin,resolvconf,resolvconf"
PACKAGECONFIG[testserv-plugin] = "--enable-testserv-plugin,--disable-testserv-plugin"

TARGET_CFLAGS += "-DFINIT_NOLOGIN_PATH=\\"${NOLOGINS_FILE}\\""

inherit autotools gettext pkgconfig update-alternatives

SRC_URI = "git://github.com/troglobit/finit;protocol=https;branch=master;name=finit"

# tag 4.13
SRCREV_finit = "85484178e068bebfb96c029471f2d135f06a7fce"

PV = "4.13+git${SRCPV}"

S = "${WORKDIR}/git"

PACKAGES =+ "${PN}-plugins ${PN}-bash-completion"

DEPENDS += "libuev libite virtual/crypt"
RDEPENDS:${PN} += "${PN}-plugins util-linux-fsck"

FILES:${PN} += "${libdir}/tmpfiles.d"
FILES:${PN}-plugins = "${libdir}/finit/plugins"
FILES:${PN}-bash-completion = "${datadir}/bash-completion"

ALTERNATIVE_PRIORITY = "100"
ALTERNATIVE:${PN} = "coldplug getty logit runparts"

ALTERNATIVE_LINK_NAME[coldplug] = "${base_sbindir}/coldplug"
ALTERNATIVE_LINK_NAME[getty] = "${base_sbindir}/getty"
ALTERNATIVE_LINK_NAME[logit] = "${base_sbindir}/logit"
ALTERNATIVE_LINK_NAME[runparts] = "${base_sbindir}/runparts"

do_install:append() {
    # Make a empty finit.conf
    install -d ${D}${sysconfdir}
    echo "" > ${D}${sysconfdir}/finit.conf

    # For compatibility with sysvinit
    install -d ${D}${base_sbindir}

    ln -sf ${sbindir}/halt ${D}${base_sbindir}/halt
    ln -sf ${sbindir}/reboot ${D}${base_sbindir}/reboot
    ln -sf ${sbindir}/shutdown ${D}${base_sbindir}/shutdown
    ln -sf ${sbindir}/finit ${D}${base_sbindir}/init
    ln -sf ${libexecdir}/finit/coldplug ${D}${base_sbindir}/coldplug
    ln -sf ${libexecdir}/finit/getty ${D}${base_sbindir}/getty
    ln -sf ${libexecdir}/finit/logit ${D}${base_sbindir}/logit
    ln -sf ${libexecdir}/finit/runparts ${D}${base_sbindir}/runparts
    ln -sf  ${localstatedir}/lib/dbus/machine-id ${D}${sysconfdir}/machine-id

    # /var/tmp in finit's tmpfiles  does not comply with OE's meta/files/fs-perms.txt
    sed -i -e "/d.*var\/tmp/d" ${D}${libdir}/tmpfiles.d/var.conf
}
