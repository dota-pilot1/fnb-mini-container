import { Module } from '@nestjs/common';
import { BrandModule } from './brand/brand.module.js';
import { DrizzleModule } from './drizzle/drizzle.module.js';

@Module({
  imports: [DrizzleModule, BrandModule],
})
export class AppModule {}
