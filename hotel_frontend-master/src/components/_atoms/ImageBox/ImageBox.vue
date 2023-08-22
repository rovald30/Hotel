<script setup>
defineProps({
  fileName: String,
  dirName: String,
  name: String,
});
</script>

<template>
  <div>
    <h1>{{ name }}</h1>
    <img
      class="my-3 rounded-lg border-2 border-gray-600"
      :src="$image(fileName, dirName)"
      :alt="name"
    />
  </div>
</template>
