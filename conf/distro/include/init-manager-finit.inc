VIRTUAL-RUNTIME_init_manager ??= "finit"
VIRTUAL-RUNTIME_initscripts ??= "finitscripts"
VIRTUAL-RUNTIME_login_manager ??= "busybox"
VIRTUAL-RUNTIME_dev_manager = ""
VIRTUAL-RUNTIME_base-utils-hwclock = ""

PREFERRED_RPROVIDER_initd-functions = "finitscripts"

# finit does not support volatile implementation from OE, disable volatile log
VOLATILE_LOG_DIR = "no"
