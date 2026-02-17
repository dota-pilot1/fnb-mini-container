import { Module } from '@nestjs/common';
import { SettlementController } from './settlement.controller.js';

@Module({
  controllers: [SettlementController],
})
export class SettlementModule {}
