<script setup>
import { getApiV2IssuersIssuerUuidBadges } from '~/services/issuer-badge-class-resource/issuer-badge-class-resource';

const toast = useToast()
const { issuerUuid } = defineProps(['issuerUuid'])
const { me } = useAuth()

const { params, setParam } = useParams('getApiV2IssuersIssuerUuidBadges' + issuerUuid + 'Params', { page: 1, sort: 'name' })
const { data: paginatedBadges, status, refresh } = useLazyAsyncData('getApiV2IssuersIssuerUuidBadges' + issuerUuid,
    () => getApiV2IssuersIssuerUuidBadges(issuerUuid, params.value),
    { transform: data => data.data })

const isMember = computed(() =>
    me.value?.memberships?.some(m => m.issuer.id === issuerUuid))

const badgeUrl = (badge) =>
    isMember.value
        ? `/organizations/${issuerUuid}/badge/${badge.id}`
        : `/badges/${badge.id}`

const prevPage = _ => setParam('page', params.value.page - 1)
const nextPage = _ => setParam('page', params.value.page + 1)
watch(params, _ => refresh())
</script>

<template>
    <div class="card card-pad">
        <div class="flex items-center justify-between gap-4 mb-6">
            <div>
                <h2 class="font-display text-lg font-bold">Credenciales</h2>
                <p class="text-sm text-ink-soft">Insignias emitidas por esta organización.</p>
            </div>
            <OnlyMembers :issuer-uuid="issuerUuid">
                <NuxtLink :to="`/organizations/${issuerUuid}/badges/new`" class="btn btn-primary btn-sm shrink-0">
                    <Icon name="material-symbols:add" class="text-lg" />
                    Nueva credencial
                </NuxtLink>
            </OnlyMembers>
        </div>

        <div v-if="status !== 'success'" class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4">
            <div v-for="i in 8" :key="i" class="skeleton aspect-square rounded-2xl"></div>
        </div>

        <div v-else-if="!paginatedBadges?.data?.length" class="empty">
            <span class="flex h-14 w-14 items-center justify-center rounded-2xl bg-teal-soft text-teal">
                <Icon name="material-symbols:workspace-premium" class="text-2xl" />
            </span>
            <p class="text-sm">No hay credenciales disponibles todavía.</p>
        </div>

        <template v-else>
            <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4">
                <NuxtLink v-for="badge in paginatedBadges.data" :key="badge.id" :to="badgeUrl(badge)"
                    class="card card-hover p-3 group">
                    <img :src="badge.jsonPayload.image" :alt="badge.name"
                        class="aspect-square w-full rounded-xl object-cover transition-transform duration-500 group-hover:scale-[1.02]" />
                    <p class="mt-3 text-sm font-semibold line-clamp-2">{{ badge.name }}</p>
                </NuxtLink>
            </div>
            <div class="mt-6">
                <AppPagination :meta="paginatedBadges?.meta" @prev="prevPage" @next="nextPage" />
            </div>
        </template>
    </div>
</template>
