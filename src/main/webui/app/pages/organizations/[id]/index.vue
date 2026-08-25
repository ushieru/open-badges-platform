<script setup>
import { MemberRole } from '~/models'
import { getApiAdminIssuersUuid } from '~/services/admin-resource/admin-resource'

const toast = useToast()
const route = useRoute()
const organizationId = route.params.id

const jsonDl = ref({})

const { data: payload, status } = useLazyAsyncData(() => getApiAdminIssuersUuid(organizationId))

watchEffect(() => {
    if (payload?.value?.status == 200)
        jsonDl.value = payload.value.data.jsonPayload
    if (payload?.value?.status == 404)
        return navigateTo('/organizations')
            .then(_ => toast.error({ title: 'Error al cargar la organización' }))
})

const copyJson = () => copy(JSON.stringify(jsonDl.value), 'JSON-LD copiado')
</script>

<template>
    <div>
        <NuxtLink to="/organizations" class="link inline-flex items-center gap-1 text-sm mb-6">
            <Icon name="material-symbols:arrow-back" class="text-lg" />
            Volver a organizaciones
        </NuxtLink>

        <div v-if="status !== 'success'" class="card card-pad flex flex-col lg:flex-row gap-8 items-center">
            <div class="skeleton h-44 w-44 rounded-3xl shrink-0"></div>
            <div class="flex-1 w-full space-y-3">
                <div class="skeleton h-8 w-1/2"></div>
                <div class="skeleton h-4 w-full"></div>
                <div class="skeleton h-4 w-2/3"></div>
            </div>
        </div>

        <template v-else>
            <!-- Header -->
            <div class="relative overflow-hidden rounded-[2.5rem] border border-line bg-surface-2 p-8 md:p-10">
                <div aria-hidden="true" class="pointer-events-none absolute -top-24 -right-24 h-80 w-80 rounded-full bg-teal/15 blur-3xl"></div>
                <div class="relative flex flex-col md:flex-row items-center gap-8">
                    <div class="rounded-[1.5rem] border border-line-strong bg-surface p-2 shrink-0">
                        <img :src="payload.data.logoUrl" :alt="`Logo de ${payload.data.name}`" class="h-40 w-40 rounded-[calc(1.5rem-0.5rem)] object-cover" />
                    </div>
                    <div class="flex-1 flex flex-col gap-3 text-center md:text-left">
                        <span class="badge badge-teal self-center md:self-start">
                            <Icon name="material-symbols:domain" class="text-base" />
                            Organización emisora
                        </span>
                        <h1 class="font-display text-3xl md:text-4xl font-bold tracking-tight">{{ payload.data.name }}</h1>
                        <p class="text-ink-soft text-balance">{{ payload.data.description }}</p>
                        <div class="flex flex-wrap items-center justify-center md:justify-start gap-4 pt-1">
                            <NuxtLink :to="payload.data.url" external target="_blank" rel="noopener"
                                class="inline-flex items-center gap-1.5 text-sm font-semibold text-teal hover:text-teal-strong">
                                <Icon name="material-symbols:language" class="text-lg" />
                                {{ payload.data.url }}
                            </NuxtLink>
                            <button @click="copy(payload.data.email, 'Email copiado')"
                                class="inline-flex items-center gap-1.5 text-sm font-semibold text-teal hover:text-teal-strong">
                                <Icon name="material-symbols:mail" class="text-lg" />
                                {{ payload.data.email }}
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Metadata + Credentials -->
            <div class="mt-6 grid grid-cols-1 lg:grid-cols-2 gap-6">
                <div class="card card-pad">
                    <div class="flex items-center justify-between mb-4">
                        <h2 class="font-display text-lg font-bold">Metadata</h2>
                        <div class="flex gap-2">
                            <button class="btn btn-outline btn-sm" @click="copyJson" aria-label="Copiar JSON-LD">
                                <Icon name="material-symbols:content-copy" class="text-lg" />
                            </button>
                            <NuxtLink external :to="jsonDl.id" target="_blank" rel="noopener" class="btn btn-outline btn-sm" aria-label="Abrir JSON-LD">
                                <Icon name="material-symbols:open-in-new" class="text-lg" />
                            </NuxtLink>
                        </div>
                    </div>
                    <div class="flex flex-col gap-4">
                        <div class="field">
                            <span class="field-label font-mono text-xs uppercase tracking-wider">@context</span>
                            <input type="text" class="input font-mono text-xs" readonly :value="jsonDl['@context']" />
                        </div>
                        <div class="grid sm:grid-cols-2 gap-4">
                            <div class="field">
                                <span class="field-label font-mono text-xs uppercase tracking-wider">Type</span>
                                <input type="text" class="input font-mono text-xs" readonly :value="jsonDl.type" />
                            </div>
                            <div class="field">
                                <span class="field-label font-mono text-xs uppercase tracking-wider">ID canónico</span>
                                <input type="text" class="input font-mono text-xs" readonly :value="organizationId" />
                            </div>
                        </div>
                        <div class="field">
                            <span class="field-label font-mono text-xs uppercase tracking-wider">Lista de revocación</span>
                            <div class="flex gap-2">
                                <input type="text" class="input font-mono text-xs flex-1" readonly :value="jsonDl.revocationList" />
                                <NuxtLink external :to="jsonDl.revocationList" target="_blank" rel="noopener" class="btn btn-outline btn-sm shrink-0" aria-label="Abrir lista de revocación">
                                    <Icon name="material-symbols:open-in-new" class="text-lg" />
                                </NuxtLink>
                            </div>
                        </div>
                    </div>
                </div>

                <OrganizationsBadges :issuer-uuid="organizationId" />
            </div>

            <!-- Members -->
            <div class="mt-6">
                <OnlySuperUsersOrMembers :issuer-uuid="organizationId" :roles="[MemberRole.OWNER]">
                    <OrganizationsMembers :issuer-uuid="organizationId" />
                </OnlySuperUsersOrMembers>
            </div>
        </template>
    </div>
</template>
