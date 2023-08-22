<script setup>
defineProps({
  textContent: String,
});
</script>

<template>
  <button
    data-testid="base-button"
    class="hover:bg-blue-500 text-gray-700 hover:text-white px-5 border-2 border-gray-800 hover:border-transparent rounded mx-0.5"
    @click="$emit('clickHandler')"
  >
    {{ textContent }}
  </button>
</template>
