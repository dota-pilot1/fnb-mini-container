import { Module } from '@nestjs/common';
import { BrandModule } from './brand/brand.module.js';
import { DrizzleModule } from './drizzle/drizzle.module.js';
import { SettlementModule } from './settlement/settlement.module.js';

@Module({
  imports: [DrizzleModule, BrandModule, SettlementModule],
})
export class AppModule {}
