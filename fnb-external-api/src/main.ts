import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module.js';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // CORS 허용 (fnb-mini-back에서 호출)
  app.enableCors();

  // 글로벌 prefix
  app.setGlobalPrefix('api');

  await app.listen(18090);
  console.log('===================================');
  console.log('  fnb-external-api (Airstar Mock)');
  console.log('  http://localhost:18090');
  console.log('===================================');
}
bootstrap();
