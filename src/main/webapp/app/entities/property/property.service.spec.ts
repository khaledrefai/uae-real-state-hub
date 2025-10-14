import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import PropertyService from './property.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Property } from '@/shared/model/property.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Property Service', () => {
    let service: PropertyService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PropertyService();
      currentDate = new Date();
      elemDefault = new Property(
        123,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'SALE',
        'Presale',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        false,
        currentDate,
        0,
        0,
        0,
        'AAAAAAA',
        0,
        0,
        'AAAAAAA',
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          completionDateTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          publishedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          updatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Property', async () => {
        const returnedFromService = {
          id: 123,
          completionDateTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          publishedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          updatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { completionDateTime: currentDate, publishedAt: currentDate, updatedAt: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Property', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Property', async () => {
        const returnedFromService = {
          extId: 1,
          slug: 'BBBBBB',
          name: 'BBBBBB',
          developer: 'BBBBBB',
          area: 'BBBBBB',
          city: 'BBBBBB',
          country: 'BBBBBB',
          listingType: 'BBBBBB',
          status: 'BBBBBB',
          saleStatus: 'BBBBBB',
          readiness: 'BBBBBB',
          serviceCharge: 'BBBBBB',
          furnishing: 'BBBBBB',
          hasEscrow: true,
          postHandover: true,
          completionDateTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          minPrice: 1,
          maxPrice: 1,
          minPriceAed: 1,
          priceCurrency: 'BBBBBB',
          minArea: 1,
          maxArea: 1,
          areaUnit: 'BBBBBB',
          latitude: 1,
          longitude: 1,
          coverUrl: 'BBBBBB',
          coverJson: 'BBBBBB',
          videoUrl: 'BBBBBB',
          brochureUrl: 'BBBBBB',
          layoutsPdfUrl: 'BBBBBB',
          website: 'BBBBBB',
          overviewMd: 'BBBBBB',
          raw: 'BBBBBB',
          publishedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          updatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };

        const expected = { completionDateTime: currentDate, publishedAt: currentDate, updatedAt: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Property', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Property', async () => {
        const patchObject = {
          extId: 1,
          name: 'BBBBBB',
          area: 'BBBBBB',
          city: 'BBBBBB',
          country: 'BBBBBB',
          status: 'BBBBBB',
          saleStatus: 'BBBBBB',
          serviceCharge: 'BBBBBB',
          furnishing: 'BBBBBB',
          hasEscrow: true,
          minPrice: 1,
          maxPrice: 1,
          minPriceAed: 1,
          priceCurrency: 'BBBBBB',
          areaUnit: 'BBBBBB',
          latitude: 1,
          longitude: 1,
          coverUrl: 'BBBBBB',
          brochureUrl: 'BBBBBB',
          layoutsPdfUrl: 'BBBBBB',
          website: 'BBBBBB',
          overviewMd: 'BBBBBB',
          raw: 'BBBBBB',
          publishedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          updatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...new Property(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { completionDateTime: currentDate, publishedAt: currentDate, updatedAt: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Property', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Property', async () => {
        const returnedFromService = {
          extId: 1,
          slug: 'BBBBBB',
          name: 'BBBBBB',
          developer: 'BBBBBB',
          area: 'BBBBBB',
          city: 'BBBBBB',
          country: 'BBBBBB',
          listingType: 'BBBBBB',
          status: 'BBBBBB',
          saleStatus: 'BBBBBB',
          readiness: 'BBBBBB',
          serviceCharge: 'BBBBBB',
          furnishing: 'BBBBBB',
          hasEscrow: true,
          postHandover: true,
          completionDateTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          minPrice: 1,
          maxPrice: 1,
          minPriceAed: 1,
          priceCurrency: 'BBBBBB',
          minArea: 1,
          maxArea: 1,
          areaUnit: 'BBBBBB',
          latitude: 1,
          longitude: 1,
          coverUrl: 'BBBBBB',
          coverJson: 'BBBBBB',
          videoUrl: 'BBBBBB',
          brochureUrl: 'BBBBBB',
          layoutsPdfUrl: 'BBBBBB',
          website: 'BBBBBB',
          overviewMd: 'BBBBBB',
          raw: 'BBBBBB',
          publishedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          updatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { completionDateTime: currentDate, publishedAt: currentDate, updatedAt: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Property', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Property', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Property', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
