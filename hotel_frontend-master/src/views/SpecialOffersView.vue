<script setup>
import Navbar from "@/components/_organisms/Navbar/Navbar.vue";
import Product from "@/components/_organisms/Product/Product.vue";
import ContentWrapper from "@/components/_molecules/ContentWrapper/ContentWrapper.vue";

/*
Products are currently hard-coded, but perhaps 
we should request the same information from the database?
*/
const products = [
  { name: "Parking", image: "parking.jpg", price: 10 },
  { name: "Champagne", image: "champagne.jpeg", price: 15 },
  { name: "Shuttle", image: "shuttle.png", price: 5 },
  { name: "Massage", image: "massage.jpg", price: 50 },
  { name: "Restaurant", image: "restaurant.jpg", price: 25 },
  { name: "Wake up call", image: "wake-up-call.jpg", price: 5 },
];
const imagesDir = "service-imgs";
</script>

<template>
  <Navbar />
  <ContentWrapper>
    <p>Special Offers</p>
    <div class="flex flex-wrap place-content-around">
      <Product
        v-for="product in products"
        :key="product"
        :product-name="product.name"
        product-description="Enjoy this wonderful special offer on our behalf."
        :productPrice="product.price"
        :file-name="product.image"
        :dir-name="imagesDir"
      />
    </div>
  </ContentWrapper>
</template>

<style scoped>
p {
  padding: 50px 0 50px 0;
}
</style>
