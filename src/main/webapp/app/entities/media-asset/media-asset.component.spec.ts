import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import MediaAsset from './media-asset.vue';
import MediaAssetService from './media-asset.service';
import AlertService from '@/shared/alert/alert.service';

type MediaAssetComponentType = InstanceType<typeof MediaAsset>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('MediaAsset Management Component', () => {
    let mediaAssetServiceStub: SinonStubbedInstance<MediaAssetService>;
    let mountOptions: MountingOptions<MediaAssetComponentType>['global'];

    beforeEach(() => {
      mediaAssetServiceStub = sinon.createStubInstance<MediaAssetService>(MediaAssetService);
      mediaAssetServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          mediaAssetService: () => mediaAssetServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mediaAssetServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(MediaAsset, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(mediaAssetServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.mediaAssets[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(MediaAsset, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(mediaAssetServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: MediaAssetComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(MediaAsset, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        mediaAssetServiceStub.retrieve.reset();
        mediaAssetServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        mediaAssetServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(mediaAssetServiceStub.retrieve.called).toBeTruthy();
        expect(comp.mediaAssets[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(mediaAssetServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        mediaAssetServiceStub.retrieve.reset();
        mediaAssetServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(mediaAssetServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.mediaAssets[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(mediaAssetServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        mediaAssetServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeMediaAsset();
        await comp.$nextTick(); // clear components

        // THEN
        expect(mediaAssetServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(mediaAssetServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
