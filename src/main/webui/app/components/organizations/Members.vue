<script setup>
import { MemberRole } from '~/models/memberRole'
import { getApiV2IssuersIssuerUuidMembers } from '~/services/issuer-resource/issuer-resource';
import { deleteApiMembershipsIssuersIssuerUuidAccountsAccountUuid, postApiMembershipsIssuersIssuerUuidAccounts } from '~/services/membership-resource/membership-resource';

const toast = useToast()
const { issuerUuid } = defineProps(['issuerUuid'])
const { me, fetchMe } = useAuth()

const { params, setParam } = useParams('getApiV2IssuersIssuerUuidMembers' + 'Params', { page: 1, sort: 'name' })
const { data: paginatedMembers, status, refresh } = useLazyAsyncData(() => getApiV2IssuersIssuerUuidMembers(issuerUuid, params.value),
    { transform: data => data.data })

const createDialogOpen = ref(false)
const memberToDelete = ref(null)

const prevPage = _ => setParam('page', params.value.page - 1)
const nextPage = _ => setParam('page', params.value.page + 1)
watch(params, _ => refresh())

const createNewMember = (e) => postApiMembershipsIssuersIssuerUuidAccounts(issuerUuid, {
    email: e.target.email.value,
    role: e.target.role.value,
})
    .then(({ data, status }) => status != 200 ? Promise.reject(data) : Promise.resolve(data))
    .then(data => me.value.account.email == data.account.email ? fetchMe() : Promise.resolve())
    .then(_ => e.target.reset())
    .then(_ => refresh())
    .then(_ => { createDialogOpen.value = false; toast.success({ title: 'Miembro creado' }) })
    .catch(data => toast.error({ title: data.message }))

const deleteMember = (member) => deleteApiMembershipsIssuersIssuerUuidAccountsAccountUuid(member.issuer.id, member.account.id)
    .then(_ => me.value.account.email == member.account.email ? fetchMe() : Promise.resolve())
    .then(_ => refresh())
    .then(_ => { memberToDelete.value = null; toast.success({ title: 'Miembro eliminado' }) })
</script>

<template>
    <div class="card overflow-hidden">
        <div class="card-pad pb-0 flex items-center justify-between gap-4">
            <div>
                <h2 class="font-display text-lg font-bold">Miembros</h2>
                <p class="text-sm text-ink-soft">Personas con acceso a esta organización.</p>
            </div>
            <button class="btn btn-primary btn-sm shrink-0" @click="createDialogOpen = true">
                <Icon name="material-symbols:person-add" class="text-lg" />
                Nuevo miembro
            </button>
        </div>

        <div class="overflow-x-auto">
            <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Nombre</th>
                        <th>Email</th>
                        <th>Rol</th>
                        <th class="text-right">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="status !== 'success'">
                        <td colspan="5">
                            <div class="flex items-center justify-center gap-3 py-8">
                                <span class="spinner"></span>
                                <span class="text-sm text-ink-soft">Cargando miembros...</span>
                            </div>
                        </td>
                    </tr>
                    <tr v-else-if="!paginatedMembers?.data?.length">
                        <td colspan="5">
                            <div class="empty">
                                <span class="flex h-14 w-14 items-center justify-center rounded-2xl bg-teal-soft text-teal">
                                    <Icon name="material-symbols:group" class="text-2xl" />
                                </span>
                                <p class="text-sm">Sin miembros todavía.</p>
                            </div>
                        </td>
                    </tr>
                    <tr v-else v-for="membership in paginatedMembers?.data" :key="membership.id">
                        <td>
                            <button class="link font-mono text-xs" @click="copy(membership.id, 'ID copiado')">{{ membership.id.slice(0, 8) }}...</button>
                        </td>
                        <td class="font-medium">{{ membership.account.fullName }}</td>
                        <td>
                            <button class="link text-sm" @click="copy(membership.account.email, 'Email copiado')">{{ membership.account.email }}</button>
                        </td>
                        <td>
                            <span class="badge" :class="membership.role === MemberRole.OWNER ? 'badge-gold' : 'badge-teal'">
                                {{ membership.role }}
                            </span>
                        </td>
                        <td class="text-right">
                            <button class="btn btn-outline btn-sm text-danger" :aria-label="`Eliminar a ${membership.account.fullName}`"
                                @click="memberToDelete = membership">
                                <Icon name="material-symbols:delete" class="text-lg" />
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="border-t border-line p-4">
            <AppPagination :meta="paginatedMembers?.meta" @prev="prevPage" @next="nextPage" />
        </div>
    </div>

    <!-- Create member dialog -->
    <Teleport to="body">
        <div v-if="createDialogOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60" @click.self="createDialogOpen = false">
            <div class="w-full max-w-md rounded-3xl border border-line bg-surface p-6 shadow-card-hover" role="dialog" aria-modal="true" aria-labelledby="create-member-title">
                <div class="flex items-center justify-between mb-5">
                    <h3 id="create-member-title" class="font-display text-lg font-bold">Nuevo miembro</h3>
                    <button class="btn btn-ghost btn-icon" aria-label="Cerrar" @click="createDialogOpen = false">
                        <Icon name="material-symbols:close" class="text-xl" />
                    </button>
                </div>
                <form @submit.prevent="createNewMember" class="flex flex-col gap-5">
                    <div class="field">
                        <label class="field-label" for="member-email">Email</label>
                        <input id="member-email" type="email" name="email" class="input" required />
                    </div>
                    <div class="field">
                        <label class="field-label" for="member-role">Rol</label>
                        <select id="member-role" name="role" class="select" required>
                            <option disabled selected>Selecciona un rol</option>
                            <option :value="value" v-for="value in MemberRole" :key="value">{{ value }}</option>
                        </select>
                    </div>
                    <button class="btn btn-primary btn-block mt-2">Crear miembro</button>
                </form>
            </div>
        </div>
    </Teleport>

    <!-- Delete member dialog -->
    <Teleport to="body">
        <div v-if="memberToDelete" class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60" @click.self="memberToDelete = null">
            <div class="w-full max-w-md rounded-3xl border border-line bg-surface p-6 shadow-card-hover" role="dialog" aria-modal="true">
                <div class="flex items-center justify-between mb-4">
                    <h3 class="font-display text-lg font-bold">Eliminar miembro</h3>
                    <button class="btn btn-ghost btn-icon" aria-label="Cerrar" @click="memberToDelete = null">
                        <Icon name="material-symbols:close" class="text-xl" />
                    </button>
                </div>
                <p class="text-sm text-ink-soft">
                    ¿Confirmas la eliminación de <strong class="text-ink">{{ memberToDelete?.account?.fullName }}</strong>?
                </p>
                <div class="mt-6 flex gap-3 justify-end">
                    <button class="btn btn-ghost" @click="memberToDelete = null">Cancelar</button>
                    <button class="btn btn-danger" @click="deleteMember(memberToDelete)">Eliminar miembro</button>
                </div>
            </div>
        </div>
    </Teleport>
</template>
