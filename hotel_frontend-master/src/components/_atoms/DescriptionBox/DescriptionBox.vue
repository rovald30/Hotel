<script setup>
defineProps({
  description: String,
  price: Number,
});
</script>

<template>
  <div>
    <p class="italic">{{ description }}</p>
    <p>Our price for you: {{ price }}€</p>
  </div>
</template>
