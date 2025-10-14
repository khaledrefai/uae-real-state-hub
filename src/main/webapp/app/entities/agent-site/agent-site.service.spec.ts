import axios from 'axios';
import sinon from 'sinon';

import AgentSiteService from './agent-site.service';
import { AgentSite } from '@/shared/model/agent-site.model';

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
  describe('AgentSite Service', () => {
    let service: AgentSiteService;
    let elemDefault;

    beforeEach(() => {
      service = new AgentSiteService();
      elemDefault = new AgentSite(
        123,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { ...elemDefault };
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

      it('should create a AgentSite', async () => {
        const returnedFromService = { id: 123, ...elemDefault };
        const expected = { ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a AgentSite', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a AgentSite', async () => {
        const returnedFromService = {
          slug: 'BBBBBB',
          displayName: 'BBBBBB',
          theme: 'BBBBBB',
          primaryColor: 'BBBBBB',
          secondaryColor: 'BBBBBB',
          logoUrl: 'BBBBBB',
          contactEmail: 'BBBBBB',
          contactPhone: 'BBBBBB',
          contactWhatsapp: 'BBBBBB',
          domain: 'BBBBBB',
          isActive: true,
          ...elemDefault,
        };

        const expected = { ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a AgentSite', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a AgentSite', async () => {
        const patchObject = {
          displayName: 'BBBBBB',
          primaryColor: 'BBBBBB',
          secondaryColor: 'BBBBBB',
          logoUrl: 'BBBBBB',
          contactWhatsapp: 'BBBBBB',
          domain: 'BBBBBB',
          ...new AgentSite(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a AgentSite', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of AgentSite', async () => {
        const returnedFromService = {
          slug: 'BBBBBB',
          displayName: 'BBBBBB',
          theme: 'BBBBBB',
          primaryColor: 'BBBBBB',
          secondaryColor: 'BBBBBB',
          logoUrl: 'BBBBBB',
          contactEmail: 'BBBBBB',
          contactPhone: 'BBBBBB',
          contactWhatsapp: 'BBBBBB',
          domain: 'BBBBBB',
          isActive: true,
          ...elemDefault,
        };
        const expected = { ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of AgentSite', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a AgentSite', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a AgentSite', async () => {
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
