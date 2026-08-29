<script setup>
import { getApiAdminBadgesUuid } from '~/services/admin-resource/admin-resource'

const route = useRoute()
const organizationId = route.params.id
const badgeId = route.params.badgeId

const { data: payload, status } = useLazyAsyncData(() => getApiAdminBadgesUuid(badgeId))
</script>

<template>
    <div>
        <NuxtLink :to="`/organizations/${organizationId}`" class="link inline-flex items-center gap-1 text-sm mb-6">
            <Icon name="material-symbols:arrow-back" class="text-lg" />
            Volver a la organización
        </NuxtLink>

        <div v-if="status !== 'success'" class="flex flex-col gap-6">
            <div class="card card-pad space-y-3">
                <div class="skeleton h-8 w-1/2"></div>
                <div class="skeleton h-4 w-2/3"></div>
            </div>
            <div class="card overflow-hidden p-6">
                <div v-for="i in 4" :key="i" class="skeleton h-12 rounded-lg mb-3"></div>
            </div>
        </div>

        <template v-else>
            <div class="page-head flex-row items-end justify-between gap-4 flex-wrap">
                <div>
                    <span class="badge badge-teal mb-2">
                        <Icon name="material-symbols:workspace-premium" class="text-base" />
                        Emisiones
                    </span>
                    <h1 class="page-title">{{ payload.data.name }}</h1>
                    <p class="page-sub">Recipientes y estado de reclamación de esta credencial.</p>
                </div>
                <NuxtLink :to="`/badges/${badgeId}`" class="btn btn-outline btn-sm shrink-0">
                    <Icon name="material-symbols:visibility" class="text-lg" />
                    Ver credencial
                </NuxtLink>
            </div>

            <OrganizationsIssuanceRecipients :issuer-uuid="organizationId" :badge-class-uuid="badgeId" />
        </template>
    </div>
</template>