<script setup>
import { getApiMeAccountConfirmLinkedEmailsCode, postApiMeAccountLinkedEmails, putApiMeAccount } from '~/services/my-account-resource/my-account-resource';

const { me, fetchMe } = useAuth()
const toast = useToast()
const router = useRouter()
const route = useRoute()

const editNameOpen = ref(false)
const linkEmailOpen = ref(false)
const confirmEmailOpen = ref(false)

const onEditNameSubmit = (e) => putApiMeAccount({ name: e.target.name.value })
    .then(_ => fetchMe())
    .then(_ => e.target.reset())
    .then(_ => { editNameOpen.value = false })
    .then(_ => toast.success({ message: 'Nombre actualizado' }))
    .catch(_ => toast.error({ message: 'Error al actualizar el nombre' }))

const onLinkEmailSubmit = (e) => postApiMeAccountLinkedEmails({ email: e.target.email.value })
    .then(_ => e.target.reset())
    .then(_ => { linkEmailOpen.value = false })
    .then(_ => toast.success({ message: 'Vinculación de email en proceso, revisa tu bandeja de entrada' }))
    .catch(_ => toast.error({ message: 'Error al vincular el email' }))

const onConfirmLinkEmailSubmit = (e) => confirmEmail(e.target.code.value)

const confirmEmail = (code) => getApiMeAccountConfirmLinkedEmailsCode(code)
    .then(({ status }) => status == 200 ? Promise.resolve() : Promise.reject())
    .then(_ => toast.success({ message: 'Correo verificado con éxito. Tus insignias han sido reclamadas.' }))
    .then(_ => fetchMe())
    .catch(_ => toast.error({ message: 'Hubo un problema al verificar tu correo. El código podría haber expirado.' }))
    .finally(_ => { confirmEmailOpen.value = false })
    .finally(_ => router.replace({ query: {} }))

const initials = computed(() => {
    const name = me?.value?.account?.fullName || ''
    return name.split(' ').filter(Boolean).slice(0, 2).map(p => p[0]).join('').toUpperCase()
})

onMounted(() => {
    if (route.query.verify)
        confirmEmail(route.query.verify)
})
</script>

<template>
    <div>
        <div class="page-head">
            <h1 class="page-title">Mi perfil</h1>
            <p class="page-sub">Gestiona tu identidad y los correos asociados a tus credenciales.</p>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <!-- Identity card -->
            <div class="card card-pad flex flex-col items-center text-center gap-4 lg:col-span-1">
                <div class="relative">
                    <span class="flex h-24 w-24 items-center justify-center rounded-3xl bg-gradient-to-br from-[#0e7490] to-[#0b1b33] text-3xl font-bold text-white shadow-card-hover">
                        {{ initials || '?' }}
                    </span>
                    <span class="absolute -bottom-1 -right-1 flex h-7 w-7 items-center justify-center rounded-full bg-success text-white border-2 border-surface">
                        <Icon name="material-symbols:verified" class="text-base" />
                    </span>
                </div>
                <div class="flex flex-col gap-1">
                    <h2 class="font-display text-xl font-bold">{{ me?.account?.fullName }}</h2>
                    <p class="text-sm text-ink-soft">{{ me?.account?.email }}</p>
                </div>
                <button class="btn btn-outline btn-sm" @click="editNameOpen = true">
                    <Icon name="material-symbols:edit" class="text-lg" />
                    Editar nombre
                </button>
            </div>

            <!-- Emails -->
            <div class="card card-pad lg:col-span-2 flex flex-col gap-5">
                <div class="flex items-center justify-between gap-4 flex-wrap">
                    <h2 class="font-display text-lg font-bold">Emails vinculados</h2>
                    <div class="flex gap-2 flex-wrap">
                        <button class="btn btn-primary btn-sm" @click="linkEmailOpen = true">
                            <Icon name="material-symbols:mail" class="text-lg" />
                            Vincular email
                        </button>
                        <button class="btn btn-outline btn-sm" @click="confirmEmailOpen = true">
                            <Icon name="material-symbols:mark-email-read" class="text-lg" />
                            Confirmar código
                        </button>
                    </div>
                </div>
                <div class="divider m-0"></div>
                <ul class="flex flex-col gap-3">
                    <li class="flex items-center gap-3 rounded-xl border border-line bg-surface-2 px-4 py-3">
                        <Icon name="material-symbols:mail" class="text-xl text-teal" />
                        <span class="flex-1 text-sm font-medium">{{ me?.account?.email }}</span>
                        <span class="badge badge-gold">Predeterminado</span>
                    </li>
                    <li v-for="email in me?.linkedEmails" :key="email" class="flex items-center gap-3 rounded-xl border border-line bg-surface-2 px-4 py-3">
                        <Icon name="material-symbols:mail" class="text-xl text-teal" />
                        <span class="flex-1 text-sm font-medium">{{ email }}</span>
                    </li>
                    <li v-if="!me?.linkedEmails?.length" class="text-sm text-ink-soft py-2">
                        No tienes emails adicionales vinculados.
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <!-- Edit name dialog -->
    <Teleport to="body">
        <div v-if="editNameOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60" @click.self="editNameOpen = false">
            <div class="w-full max-w-md rounded-3xl border border-line bg-surface p-6 shadow-card-hover" role="dialog" aria-modal="true">
                <div class="flex items-center justify-between mb-5">
                    <h3 class="font-display text-lg font-bold">Actualizar nombre</h3>
                    <button class="btn btn-ghost btn-icon" aria-label="Cerrar" @click="editNameOpen = false">
                        <Icon name="material-symbols:close" class="text-xl" />
                    </button>
                </div>
                <form @submit.prevent="onEditNameSubmit" class="flex flex-col gap-5">
                    <div class="field">
                        <label class="field-label" for="name">Nombre completo</label>
                        <input id="name" type="text" name="name" class="input" required />
                    </div>
                    <button class="btn btn-primary btn-block">Actualizar</button>
                </form>
            </div>
        </div>
    </Teleport>

    <!-- Link email dialog -->
    <Teleport to="body">
        <div v-if="linkEmailOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60" @click.self="linkEmailOpen = false">
            <div class="w-full max-w-md rounded-3xl border border-line bg-surface p-6 shadow-card-hover" role="dialog" aria-modal="true">
                <div class="flex items-center justify-between mb-5">
                    <h3 class="font-display text-lg font-bold">Vincular nuevo email</h3>
                    <button class="btn btn-ghost btn-icon" aria-label="Cerrar" @click="linkEmailOpen = false">
                        <Icon name="material-symbols:close" class="text-xl" />
                    </button>
                </div>
                <form @submit.prevent="onLinkEmailSubmit" class="flex flex-col gap-5">
                    <div class="field">
                        <label class="field-label" for="new-email">Email</label>
                        <input id="new-email" type="email" name="email" class="input" required />
                    </div>
                    <button class="btn btn-primary btn-block">Vincular</button>
                </form>
            </div>
        </div>
    </Teleport>

    <!-- Confirm email dialog -->
    <Teleport to="body">
        <div v-if="confirmEmailOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60" @click.self="confirmEmailOpen = false">
            <div class="w-full max-w-md rounded-3xl border border-line bg-surface p-6 shadow-card-hover" role="dialog" aria-modal="true">
                <div class="flex items-center justify-between mb-5">
                    <h3 class="font-display text-lg font-bold">Código de confirmación</h3>
                    <button class="btn btn-ghost btn-icon" aria-label="Cerrar" @click="confirmEmailOpen = false">
                        <Icon name="material-symbols:close" class="text-xl" />
                    </button>
                </div>
                <form @submit.prevent="onConfirmLinkEmailSubmit" class="flex flex-col gap-5">
                    <div class="field">
                        <label class="field-label" for="code">Código</label>
                        <input id="code" type="text" name="code" class="input font-mono" required />
                    </div>
                    <button class="btn btn-primary btn-block">Confirmar</button>
                </form>
            </div>
        </div>
    </Teleport>
</template>
