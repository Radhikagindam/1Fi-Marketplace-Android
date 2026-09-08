# 1Fi-Marketplace-Android
# 1Fi SDE Intern Assignment - 1Fi Marketplace

## Overview
Implementation of the **1Fi Marketplace** section inside the Shop page as specified in the assignment brief.

## Architecture & Implementation
* **Shop Screen**: Implements the three required sections:
  * `Top Brands`: Blank placeholder screen.
  * `Nearby Stores`: Blank placeholder screen.
  * `1Fi Marketplace`: Fully designed and implemented feature.
* **1Fi Marketplace**:
  * Product listing displaying product brand, title, base price, and starting EMI.
  * Product details view with description and variant selector.
  * Interactive EMI tenure picker (tenure length, monthly breakdown, interest terms).
  * Sticky CTA to proceed with the selected plan.
* **Data Layer**:
  * Asynchronous `MarketplaceRepository` using Kotlin Coroutines/Flow to ensure product and EMI data are fetched dynamically rather than hardcoded directly into UI components.
  * Loading state handling.