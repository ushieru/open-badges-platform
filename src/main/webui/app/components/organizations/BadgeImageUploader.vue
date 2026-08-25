<script setup>
const emit = defineEmits(['update:image'])
const previewUrl = ref(null)
const error = ref(null)
const fileInput = ref(null)

const MAX_SIZE_MB = 2
const MAX_DIMENSION = 500

const onFileChange = (event) => {
    error.value = null
    const file = event.target.files?.[0]
    if (!file) return
    const fileSizeMB = file.size / 1048576
    if (fileSizeMB > MAX_SIZE_MB) {
        error.value = `Archivo muy pesado. Máximo ${MAX_SIZE_MB}MB.`
        resetInput()
        return
    }
    const reader = new FileReader()
    reader.onload = (e) => {
        const img = new Image()
        img.src = e.target?.result
        img.onload = () => {
            if (img.width !== img.height || img.width > MAX_DIMENSION) {
                error.value = 'Dimensiones inválidas (máx. 500x500 y cuadrada).'
                resetInput()
                return
            }
            previewUrl.value = img.src
            emit('update:image', img.src)
        }
    }
    reader.readAsDataURL(file)
}

const resetInput = () => {
    previewUrl.value = null
    emit('update:image', null)
    if (fileInput.value) fileInput.value.value = ''
}
</script>

<template>
    <div class="flex flex-col items-center gap-3">
        <label
            class="group relative flex aspect-square w-64 cursor-pointer flex-col items-center justify-center overflow-hidden rounded-[2rem] border-2 border-dashed border-line-strong bg-surface-2 transition-colors hover:border-teal">
            <img v-if="previewUrl" :src="previewUrl" class="absolute inset-0 h-full w-full object-cover" :alt="'Vista previa de la insignia'" />
            <div v-else class="flex flex-col items-center justify-center p-6 text-center">
                <span class="mb-3 flex h-14 w-14 items-center justify-center rounded-2xl bg-teal-soft text-teal transition-transform group-hover:scale-105">
                    <Icon name="material-symbols:image" class="text-2xl" />
                </span>
                <p class="text-sm font-medium text-ink">SVG/PNG cuadrado</p>
                <p class="mt-1 text-xs text-ink-soft">Máx. 500×500px (2MB)</p>
            </div>
            <input ref="fileInput" type="file" class="hidden" accept="image/png, image/svg+xml" @change="onFileChange" />
        </label>
        <p v-if="error" class="text-xs font-semibold text-danger">{{ error }}</p>
    </div>
</template>
