<script setup>
import dayjs from 'dayjs'
import { getApiV2AccountsAccountUuid } from '~/services/public-account-resource/public-account-resource'
import { getApiV2AccountsAccountUuidAssertions } from '~/services/public-account-assertion-resource/public-account-assertion-resource'

const route = useRoute()
const accountUuid = route.params.accountUuid

const { params, setParam } = useParams('getApiV2AccountsAccountUuidAssertionsParams', { page: 1, size: 12 })

const { data: profilePayload, status: profileStatus } = useLazyAsyncData(() => getApiV2AccountsAccountUuid(accountUuid))
const { data: assertionsPayload, refresh } = useLazyAsyncData(() =>
    getApiV2AccountsAccountUuidAssertions(accountUuid, params.value))

watch(params, _ => refresh())

const profile = computed(() => profilePayload.value?.status == 200 ? profilePayload.value.data : null)
const notFound = computed(() => profilePayload.value?.status == 404)
const loading = computed(() => profileStatus.value !== 'success' && !notFound.value)
const assertions = computed(() => assertionsPayload.value?.status == 200 ? assertionsPayload.value.data : null)

const initials = computed(() => {
    const name = profile.value?.fullName || ''
    return name.split(' ').filter(Boolean).slice(0, 2).map(p => p[0]).join('').toUpperCase()
})

const profileUrl = computed(() => `${window.location.origin}/u/${accountUuid}`)

const formatDate = (iso) => dayjs(iso).format('DD MMM YYYY')

const copyProfileUrl = () => copy(profileUrl.value, 'Enlace de perfil copiado')

const prevPage = _ => setParam('page', params.value.page - 1)
const nextPage = _ => setParam('page', params.value.page + 1)

useSeoMeta(() => ({
    title: profile.value ? `Credenciales de ${profile.value.fullName}` : 'Perfil de credenciales',
    ogTitle: profile.value ? `Credenciales de ${profile.value.fullName}` : 'Perfil de credenciales',
    ogDescription: profile.value
        ? `${profile.value.totalPublic} insignias verificables en Open Badges Platform`
        : 'Insignias digitales verificables',
    ogImage: assertions.value?.data?.[0]?.badgeClassImageUrl || `${window.location.origin}/brand/logo.svg`,
    ogType: 'profile',
}))
</script>

<template>
    <div class="mx-auto max-w-5xl flex flex-col gap-8">
        <!-- Not found -->
        <div v-if="notFound" class="card">
            <div class="empty">
                <span class="flex h-16 w-16 items-center justify-center rounded-2xl bg-teal-soft text-teal">
                    <Icon name="material-symbols:person-off" class="text-3xl" />
                </span>
                <h3 class="font-display text-lg font-bold text-ink">Perfil no encontrado</h3>
                <p class="max-w-sm text-sm">El perfil que buscas no existe o ha sido eliminado.</p>
                <NuxtLink to="/" class="btn btn-primary">Ir al inicio</NuxtLink>
            </div>
        </div>

        <!-- Loading -->
        <div v-else-if="loading" class="flex flex-col gap-6">
            <div class="card card-pad flex flex-col items-center gap-4 text-center">
                <div class="skeleton h-24 w-24 rounded-3xl"></div>
                <div class="skeleton h-7 w-48"></div>
                <div class="skeleton h-4 w-32"></div>
            </div>
            <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-5">
                <div v-for="i in 8" :key="i" class="card p-4">
                    <div class="skeleton aspect-square rounded-2xl"></div>
                    <div class="skeleton h-4 w-3/4 mt-4"></div>
                    <div class="skeleton h-3 w-1/2 mt-2"></div>
                </div>
            </div>
        </div>

        <!-- Profile -->
        <template v-else-if="profile">
            <div class="card card-pad flex flex-col items-center gap-5 text-center">
                <span class="flex h-24 w-24 items-center justify-center rounded-3xl bg-gradient-to-br from-[#0e7490] to-[#0b1b33] text-3xl font-bold text-white shadow-card-hover">
                    {{ initials || '?' }}
                </span>
                <div class="flex flex-col gap-1">
                    <h1 class="font-display text-2xl md:text-3xl font-bold tracking-tight">{{ profile.fullName }}</h1>
                    <p class="text-sm text-ink-soft">
                        {{ profile.totalPublic }} {{ profile.totalPublic == 1 ? 'credencial verificable' : 'credenciales verificables' }}
                    </p>
                </div>
                <button class="btn btn-primary btn-sm" @click="copyProfileUrl">
                    <Icon name="material-symbols:link" class="text-lg" />
                    Copiar enlace de perfil
                </button>
            </div>

            <!-- Empty curriculum -->
            <div v-if="!assertions?.data?.length" class="card">
                <div class="empty">
                    <span class="flex h-16 w-16 items-center justify-center rounded-2xl bg-teal-soft text-teal">
                        <Icon name="material-symbols:workspace-premium" class="text-3xl" />
                    </span>
                    <h3 class="font-display text-lg font-bold text-ink">Aún no hay credenciales públicas</h3>
                    <p class="max-w-sm text-sm">Este perfil no ha hecho públicas sus insignias hasta el momento.</p>
                </div>
            </div>

            <!-- Grid -->
            <div v-else class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-5">
                <a v-for="assertion in assertions.data" :key="assertion.id" :href="assertion.url" target="_blank"
                    rel="noopener" class="card card-hover p-4 group block">
                    <img :src="assertion.badgeClassImageUrl" :alt="assertion.badgeClassName"
                        class="aspect-square w-full rounded-2xl object-cover transition-transform duration-500 group-hover:scale-[1.02]" />
                    <div class="mt-4 flex flex-col gap-1">
                        <h3 class="font-display text-sm font-bold leading-snug line-clamp-2">{{ assertion.badgeClassName }}</h3>
                        <p class="text-xs text-ink-soft line-clamp-1">{{ assertion.issuerName }}</p>
                        <span class="mt-2 text-xs text-ink-soft tabular">{{ formatDate(assertion.issuedOn) }}</span>
                    </div>
                </a>
            </div>

            <AppPagination :meta="assertions?.meta" @prev="prevPage" @next="nextPage" />
        </template>
    </div>
</template>