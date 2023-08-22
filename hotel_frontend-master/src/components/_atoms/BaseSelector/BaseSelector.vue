<script setup>
const props = defineProps({
  labelText: String,
  options: Array,
  selectedOption: String,
});

</script>

<template>
  <div v-bind="$attrs">
    <label class="block text-gray-700 text-sm font-bold mb-2">
      {{ labelText }}
    </label>
    <div class="w-full border border-gray-400 shadow">
      <select class="w-full p-1 items-center">
        <option class="w-full" v-for="option in options" :value="option.value">
          {{ option }}
        </option>
      </select>
    </div>
  </div>
</template>

<style scoped></style>
