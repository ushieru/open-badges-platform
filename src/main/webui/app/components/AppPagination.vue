<script setup>
const props = defineProps({
    meta: { type: Object, default: null },
})
const emit = defineEmits(['prev', 'next'])

const current = computed(() => Number(props.meta?.currentPage) || 1)
const total = computed(() => Number(props.meta?.totalPages) || 1)
const hasPrev = computed(() => !!props.meta?.prevPage)
const hasNext = computed(() => !!props.meta?.nextPage)
</script>

<template>
    <nav v-if="meta && total > 1" class="flex items-center justify-center gap-2 pt-2" aria-label="Paginación">
        <button class="btn btn-outline btn-sm" :disabled="!hasPrev" @click="emit('prev')">
            <Icon name="material-symbols:chevron-left" class="text-lg" />
            Anterior
        </button>
        <span class="px-3 text-sm text-ink-soft tabular">
            Página {{ current }} de {{ total }}
        </span>
        <button class="btn btn-outline btn-sm" :disabled="!hasNext" @click="emit('next')">
            Siguiente
            <Icon name="material-symbols:chevron-right" class="text-lg" />
        </button>
    </nav>
</template>
